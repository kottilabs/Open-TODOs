package de.kottilabs.todobackend.permission;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public abstract class Roles {
	private Roles() {
		throw new AssertionError("This class is not meant to be initialized.");
	}

	public static final String ADMIN = "ADMIN";

	public static final String USER_READ = "USER_READ";
	public static final String USER_UPDATE = "USER_UPDATE";
	public static final String USER_CREATE = "USER_CREATE";

	public static final String SCOPE_READ = "SCOPE_READ";
	public static final String SCOPE_UPDATE = "SCOPE_UPDATE";
	public static final String SCOPE_CREATE = "SCOPE_CREATE";

	public static final String TODO_READ = "TODO_READ";
	public static final String TODO_UPDATE = "TODO_UPDATE";
	public static final String TODO_CREATE = "TODO_CREATE";

	public static final Set<String> ALL_ROLES;

	static {
		Field[] fields = Roles.class.getFields();
		ALL_ROLES = new HashSet<>();
		for (Field field : fields) {
			String name = field.getName();
			if (name.equals("ALL_ROLES")) {
				continue;
			}
			ALL_ROLES.add(name);
		}
	}
}
