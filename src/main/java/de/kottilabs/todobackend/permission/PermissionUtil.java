package de.kottilabs.todobackend.permission;

import java.util.HashSet;
import java.util.Set;

import lombok.ToString;

import de.kottilabs.todobackend.dao.Role;
import lombok.EqualsAndHashCode;

public abstract class PermissionUtil {
	private PermissionUtil() {
		throw new AssertionError("This class is not meant to be initialized.");
	}

//	public static GrantedAuthority grantedAuthorityOf(String role) {
//		return new TodoGrantedAuthority(role);
//	}
//
//	public static Set<GrantedAuthority> grantedAuthorityOf(Set<Role> authorities) {
//		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//		for (Role role : authorities) {
//			recursiveAdd(mappedAuthorities, role);
//		}
//		return mappedAuthorities;
//	}
//
//	private static void recursiveAdd(Set<GrantedAuthority> mappedAuthorities, Role role) {
//		mappedAuthorities.add(new TodoGrantedAuthority(role.getRole()));
//		for (Role inheritedRole : role.getInherited()) {
//			recursiveAdd(mappedAuthorities, inheritedRole);
//		}
//	}

//	@EqualsAndHashCode
//	@ToString
//	private static class TodoGrantedAuthority implements GrantedAuthority {
//
//		private String role;
//
//		public TodoGrantedAuthority(String role) {
//			this.role = role;
//		}
//
//		@Override
//		public String getAuthority() {
//			return role;
//		}
//	}
}
