package de.kottilabs.todobackend.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable().csrf().disable()//
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
				.and().authorizeRequests()//
				.antMatchers("/api/auth/login").permitAll()//
				.anyRequest().authenticated()//
				.and().exceptionHandling().authenticationEntryPoint(//
						(httpServletRequest, httpServletResponse, e) -> {
							httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
									"Token is invalid or expired");
						})//
				.and().apply(new JwtConfigurer(jwtTokenProvider));
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**");
	}
}
