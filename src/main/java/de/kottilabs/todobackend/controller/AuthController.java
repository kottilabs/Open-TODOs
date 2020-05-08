package de.kottilabs.todobackend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.kottilabs.todobackend.advice.BadLoginException;
import de.kottilabs.todobackend.config.JwtTokenProvider;
import de.kottilabs.todobackend.dao.User;
import de.kottilabs.todobackend.dao.UserRepository;
import de.kottilabs.todobackend.dto.AuthRequest;
import de.kottilabs.todobackend.permission.PermissionUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Map<Object, Object>> login(@RequestBody AuthRequest data) {
		try {
			String username = data.getUsername();
			User user = this.userRepository.findByUsername(username)
					.orElseThrow(() -> new AuthenticationCredentialsNotFoundException(""));

			if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
				throw new BadCredentialsException("");
			}

			Set<String> authorities = PermissionUtil.grantedAuthorityOf(user.getAuthorities());
			String token = jwtTokenProvider.createToken(username, user.getPassword(), authorities);
			Map<Object, Object> model = new HashMap<>();
			model.put("username", username);
			model.put("token", token);
			model.put("authorities", authorities);
			return ResponseEntity.ok(model);
		} catch (AuthenticationException e) {
			throw new BadLoginException();
		}
	}
}
