package de.kottilabs.todobackend.config;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.Enumeration;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable().csrf().disable()//
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
				.and().authorizeRequests()//
				.antMatchers("/api/auth/login").permitAll()//
				.anyRequest().authenticated()//
				.and().exceptionHandling().authenticationEntryPoint(//
						(httpServletRequest, httpServletResponse, e) -> {
							final String expired = (String) httpServletRequest.getAttribute("expired");
							if (expired != null) {
								httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, expired);
							} else {
								log.warn("Not expected", e);
								httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
										"Invalid username/password supplied");
							}
						})//
				.and().apply(new JwtConfigurer(jwtTokenProvider));
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**");
	}
}
