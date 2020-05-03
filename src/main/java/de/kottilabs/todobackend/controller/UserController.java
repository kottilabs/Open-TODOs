package de.kottilabs.todobackend.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import de.kottilabs.todobackend.dao.Role;
import de.kottilabs.todobackend.dao.User;
import de.kottilabs.todobackend.dto.UserRequest;
import de.kottilabs.todobackend.dto.UserResponse;
import de.kottilabs.todobackend.service.RoleService;
import de.kottilabs.todobackend.service.UserService;

@RestController
@RequestMapping(value = { "/api/user" })
public class UserController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	// @Secured(Roles.USER_READ)
	@RequestMapping(method = RequestMethod.GET)
	public List<UserResponse> find() {
		List<User> results = userService.find();
		return results.stream().map(this::convert).collect(Collectors.toList());
	}

	// @Secured(Roles.USER_CREATE)
	@RequestMapping(method = RequestMethod.POST)
	public UserResponse create(@RequestBody @Validated final UserRequest user) {
		return convert(userService.save(convert(user)));
	}

	// @Secured(Roles.USER_UPDATE)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public UserResponse update(@PathVariable final UUID id, @RequestBody @Validated final UserRequest user) {
		return convert(userService.update(id, convert(user)));
	}

	// @Secured(Roles.USER_READ)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public UserResponse get(@PathVariable final UUID id) {
		return convert(userService.findById(id));
	}

	@RequestMapping(value = "/self", method = RequestMethod.PUT)
	public UserResponse updateSelf(@RequestBody @Validated final UserRequest user) {
		// todo use Authentication
		return convert(userService.update("admin", convert(user)));
	}

	@RequestMapping(value = "/self", method = RequestMethod.GET)
	public UserResponse getSelf() {
		// todo use Authentication
		return convert(userService.findByUsername("admin"));
	}

	// @Secured(Roles.USER_CREATE)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public UserResponse delete(@PathVariable final UUID id) {
		return convert(userService.delete(id));
	}

	private UserResponse convert(User user) {
		UserResponse mapped = modelMapper.map(user, UserResponse.class);
		mapped.setPassword(null);
		mapped.setAuthorities(user.getAuthorities().stream().map(Role::getRole).collect(Collectors.toSet()));
		return mapped;
	}

	private User convert(UserRequest user) {
		User mapped = modelMapper.map(user, User.class);
		// mapped.setPassword(passwordEncoder.encode(user.getPassword()));
		mapped.setAuthorities(roleService.findById(user.getAuthorities()));
		return mapped;
	}
}
