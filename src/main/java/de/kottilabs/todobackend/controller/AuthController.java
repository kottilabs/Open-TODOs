package de.kottilabs.todobackend.controller;

import java.util.Set;

import de.kottilabs.todobackend.dao.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.kottilabs.todobackend.advice.BadLoginException;
import de.kottilabs.todobackend.config.JwtTokenProvider;
import de.kottilabs.todobackend.dao.User;
import de.kottilabs.todobackend.dto.AuthLogoutResponse;
import de.kottilabs.todobackend.dto.AuthRequest;
import de.kottilabs.todobackend.dto.AuthResponse;
import de.kottilabs.todobackend.permission.PermissionUtil;
import de.kottilabs.todobackend.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public AuthResponse login(@RequestBody AuthRequest data) {
		try {
			String username = data.getUsername();
			User user = this.userService.findByUsernameOrNull(username);
			if (user == null) {
				throw new AuthenticationCredentialsNotFoundException("");
			}

			if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
				throw new BadCredentialsException("");
			}

			Set<String> authorities = PermissionUtil.grantedAuthorityOf(user.getAuthorities());
			String token = jwtTokenProvider.createToken(username, user.getPassword(), authorities);
			AuthResponse authResponse = new AuthResponse();
			authResponse.setUsername(username);
			authResponse.setToken(token);
			authResponse.setAuthorities(authorities);
			return authResponse;
		} catch (AuthenticationException e) {
			throw new BadLoginException();
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public AuthLogoutResponse logout(Authentication authentication) {
		AuthToken authToken = (AuthToken) authentication.getCredentials();
		return convert(userService.deleteByAuthToken(authToken.getUsername(), authToken.getIssuedAt()));
	}

	@RequestMapping(value = "/logoutAll", method = RequestMethod.POST)
	public AuthLogoutResponse logoutAll(Authentication authentication) {
		AuthToken authToken = (AuthToken) authentication.getCredentials();
		return convert(userService.deleteByUsername(authToken.getUsername()));
	}

	private AuthLogoutResponse convert(long invalidated) {
		AuthLogoutResponse authLogoutResponse = new AuthLogoutResponse();
		authLogoutResponse.setTokensInvalidated(invalidated);
		return authLogoutResponse;
	}
}
