package de.kottilabs.todobackend.permission;

import java.util.HashSet;
import java.util.Set;

import de.kottilabs.todobackend.dao.Role;

public abstract class PermissionUtil {
	private PermissionUtil() {
		throw new AssertionError("This class is not meant to be initialized.");
	}

	public static Set<String> grantedAuthorityOf(Set<Role> authorities) {
		Set<String> mappedAuthorities = new HashSet<>();
		for (Role role : authorities) {
			recursiveAdd(mappedAuthorities, role);
		}
		return mappedAuthorities;
	}

	private static void recursiveAdd(Set<String> mappedAuthorities, Role role) {
		mappedAuthorities.add(role.getRole());
		for (Role inheritedRole : role.getInherited()) {
			recursiveAdd(mappedAuthorities, inheritedRole);
		}
	}
}
