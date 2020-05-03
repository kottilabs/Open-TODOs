package de.kottilabs.todobackend.config;

import org.springframework.beans.factory.annotation.Autowired;

import de.kottilabs.todobackend.service.UserService;

//@Component
public class TodoDatabaseUserDetailsService // implements UserDetailsService {
{
	@Autowired
	private final UserService userService;

	public TodoDatabaseUserDetailsService(UserService userService) {
		this.userService = userService;
	}

//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // (1)
//
//		User user = userService.findByUsernameOrNull(username);
//		if (user == null) {
//			throw new UsernameNotFoundException("Username " + username + " not found.");
//		}
//
//		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
//				PermissionUtil.grantedAuthorityOf(user.getAuthorities()));
//	}
}
