package de.kottilabs.todobackend.config;

import de.kottilabs.todobackend.dao.User;
import de.kottilabs.todobackend.permission.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;

import de.kottilabs.todobackend.service.UserService;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Primary
public class TodoDatabaseUserDetailsService implements UserDetailsService {
	@Autowired
	private final UserService userService;

	public TodoDatabaseUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // (1)

		User user = userService.findByUsernameOrNull(username);
		if (user == null) {
			throw new UsernameNotFoundException("Username " + username + " not found.");
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				PermissionUtil.grantedAuthorityOf(user.getAuthorities()).stream().map(SimpleGrantedAuthority::new)
						.collect(Collectors.toSet()));
	}
}
