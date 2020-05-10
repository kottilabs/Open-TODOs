package de.kottilabs.todobackend.controller;

import java.util.Set;

import io.swagger.annotations.ApiParam;
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
import de.kottilabs.todobackend.config.SpringFoxConfig;
import de.kottilabs.todobackend.dao.AuthToken;
import de.kottilabs.todobackend.dao.User;
import de.kottilabs.todobackend.dto.AuthLogoutResponse;
import de.kottilabs.todobackend.dto.AuthRequest;
import de.kottilabs.todobackend.dto.AuthResponse;
import de.kottilabs.todobackend.permission.PermissionUtil;
import de.kottilabs.todobackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/auth")
@Api(tags = SpringFoxConfig.AUTHENTICATION)
public class AuthController {
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@ApiOperation("Login")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public AuthResponse login(@ApiParam(value = "Login credentials", required = true) @RequestBody AuthRequest data) {
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

	@ApiOperation("Logout a specific token")
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public AuthLogoutResponse logout(@ApiIgnore Authentication authentication) {
		AuthToken authToken = (AuthToken) authentication.getCredentials();
		return convert(userService.deleteByAuthToken(authToken.getUsername(), authToken.getIssuedAt()));
	}

	@ApiOperation("Logout all tokens from my username")
	@RequestMapping(value = "/logoutAll", method = RequestMethod.POST)
	public AuthLogoutResponse logoutAll(@ApiIgnore Authentication authentication) {
		AuthToken authToken = (AuthToken) authentication.getCredentials();
		return convert(userService.deleteByUsername(authToken.getUsername()));
	}

	private AuthLogoutResponse convert(long invalidated) {
		AuthLogoutResponse authLogoutResponse = new AuthLogoutResponse();
		authLogoutResponse.setTokensInvalidated(invalidated);
		return authLogoutResponse;
	}
}
