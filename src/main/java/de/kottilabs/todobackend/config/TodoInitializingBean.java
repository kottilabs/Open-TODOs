package de.kottilabs.todobackend.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

import de.kottilabs.todobackend.dao.Role;
import de.kottilabs.todobackend.dao.User;
import de.kottilabs.todobackend.permission.Roles;
import de.kottilabs.todobackend.service.RoleService;
import de.kottilabs.todobackend.service.UserService;
import net.bytebuddy.utility.RandomString;

@Component
public class TodoInitializingBean implements InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(TodoInitializingBean.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Value("${admin.username}")
	private String adminUsername;
	@Value("${admin.password}")
	private String adminPassword;
	@Value("${admin.displayname}")
	private String adminDisplayname;

	private String jwtSecret;

	@Autowired
	private ObjectMapper objectMapper;

	public String getJwtSecret() {
		return jwtSecret;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setUpRandomSecret();
		Role adminRole = setUpRoles();
		updateAdminUser(adminRole);
	}

	private void setUpRandomSecret() {
		Path path = Paths.get("secret/secret");
		if (path.toFile().exists()) {
			try {
				jwtSecret = new String(Files.readAllBytes(path));
				log.info("Using existing secret");
			} catch (IOException e) {
				log.warn("Could not read secret", e);
			}
		}

		if (jwtSecret == null) {
			log.info("Generating new secret");
			jwtSecret = RandomString.make(32);
			try {
				PrintWriter printWriter = new PrintWriter(path.toFile());
				printWriter.write(jwtSecret);
				printWriter.close();
			} catch (FileNotFoundException e) {
				log.warn("Could not store secret", e);
			}
		}
	}

	private Role setUpRoles() {
		// User
		// ----------------------------------------------------------------

		// USER_READ
		Role user_read = roleService.findByIdOrNull(Roles.USER_READ);
		if (user_read == null) {
			user_read = new Role();
		}
		user_read.setRole(Roles.USER_READ);
		user_read = roleService.save(user_read);

		// USER_UPDATE
		Role user_update = roleService.findByIdOrNull(Roles.USER_UPDATE);
		if (user_update == null) {
			user_update = new Role();
		}
		user_update.setRole(Roles.USER_UPDATE);
		user_update.setInherited(Sets.newHashSet(user_read));
		user_update = roleService.save(user_update);

		// USER_CREATE
		Role user_create = roleService.findByIdOrNull(Roles.USER_CREATE);
		if (user_create == null) {
			user_create = new Role();
		}
		user_create.setRole(Roles.USER_CREATE);
		user_create.setInherited(Sets.newHashSet(user_update));
		user_create = roleService.save(user_create);
		// ----------------------------------------------------------------

		// Scope
		// ----------------------------------------------------------------

		// SCOPE_READ
		Role scope_read = roleService.findByIdOrNull(Roles.SCOPE_READ);
		if (scope_read == null) {
			scope_read = new Role();
		}
		scope_read.setRole(Roles.SCOPE_READ);
		scope_read = roleService.save(scope_read);

		// SCOPE_UPDATE
		Role scope_update = roleService.findByIdOrNull(Roles.SCOPE_UPDATE);
		if (scope_update == null) {
			scope_update = new Role();
		}
		scope_update.setRole(Roles.SCOPE_UPDATE);
		scope_update.setInherited(Sets.newHashSet(scope_read));
		scope_update = roleService.save(scope_update);

		// SCOPE_CREATE
		Role scope_create = roleService.findByIdOrNull(Roles.SCOPE_CREATE);
		if (scope_create == null) {
			scope_create = new Role();
		}
		scope_create.setRole(Roles.SCOPE_CREATE);
		scope_create.setInherited(Sets.newHashSet(scope_update));
		scope_create = roleService.save(scope_create);
		// ----------------------------------------------------------------

		// Todos
		// ----------------------------------------------------------------

		// TODO_READ
		Role todo_read = roleService.findByIdOrNull(Roles.TODO_READ);
		if (todo_read == null) {
			todo_read = new Role();
		}
		todo_read.setRole(Roles.TODO_READ);
		todo_read = roleService.save(todo_read);

		// TODO_UPDATE
		Role todo_update = roleService.findByIdOrNull(Roles.TODO_UPDATE);
		if (todo_update == null) {
			todo_update = new Role();
		}
		todo_update.setRole(Roles.TODO_UPDATE);
		todo_update.setInherited(Sets.newHashSet(todo_read));
		todo_update = roleService.save(todo_update);

		// TODO_CREATE
		Role todo_create = roleService.findByIdOrNull(Roles.TODO_CREATE);
		if (todo_create == null) {
			todo_create = new Role();
		}
		todo_create.setRole(Roles.TODO_CREATE);
		todo_create.setInherited(Sets.newHashSet(todo_update));
		todo_create = roleService.save(todo_create);
		// ----------------------------------------------------------------

		// Admin
		// ----------------------------------------------------------------
		Role admin = roleService.findByIdOrNull(Roles.ADMIN);
		if (admin == null) {
			admin = new Role();
		}
		admin.setRole(Roles.ADMIN);
		admin.setInherited(Sets.newHashSet(user_create, scope_create, todo_create));
		roleService.save(admin);
		// ----------------------------------------------------------------

		// cleanup non existing roles
		roleService.deleteNotIn(Roles.ALL_ROLES);

		try {
			log.info("Available Roles:\n\n{}\n",
					objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(roleService.find()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return admin;
	}

	private void updateAdminUser(Role adminRole) {
		User user = userService.findByUsernameOrNull(adminUsername);
		if (user == null) {
			user = new User();
		}
		user.setUsername(adminUsername);
		String password = passwordEncoder.encode(adminPassword);
		user.setPassword(password);
		user.setDisplayname(adminDisplayname);
		user.setAuthorities(Sets.newHashSet(adminRole));
		try {
			log.info("Store admin user: \n\n{}\n",
					objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		userService.save(user);
	}
}
