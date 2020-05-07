package de.kottilabs.todobackend.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtTokenFilter extends GenericFilterBean {
	private JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
		if (token != null) {
			if (jwtTokenProvider.validateToken(token)) {
				Authentication auth = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			} else {
				req.setAttribute("expired", "Token is invalid or expired");
			}
		}
		filterChain.doFilter(req, res);
	}
}
