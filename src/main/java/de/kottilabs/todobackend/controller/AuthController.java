package de.kottilabs.todobackend.controller;

import de.kottilabs.todobackend.config.JwtTokenProvider;
import de.kottilabs.todobackend.dao.Role;
import de.kottilabs.todobackend.dao.User;
import de.kottilabs.todobackend.dao.UserRepository;
import de.kottilabs.todobackend.dto.AuthRequest;
import de.kottilabs.todobackend.permission.PermissionUtil;
import de.kottilabs.todobackend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<Map<Object, Object>> login(@RequestBody AuthRequest data) {
		try {
			String username = data.getUsername();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
			User user = this.userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found"));

			Set<String> authorities = PermissionUtil.grantedAuthorityOf(user.getAuthorities());
			String token = jwtTokenProvider.createToken(username, new ArrayList<>(authorities));
			Map<Object, Object> model = new HashMap<>();
			model.put("username", username);
			model.put("token", token);
			model.put("authorities", authorities);
			return ResponseEntity.ok(model);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid username/password supplied");
		}
	}
}
