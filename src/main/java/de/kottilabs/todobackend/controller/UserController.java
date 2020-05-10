package de.kottilabs.todobackend.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import de.kottilabs.todobackend.config.SpringFoxConfig;
import de.kottilabs.todobackend.dao.Role;
import de.kottilabs.todobackend.dao.User;
import de.kottilabs.todobackend.dto.UserRequest;
import de.kottilabs.todobackend.dto.UserResponse;
import de.kottilabs.todobackend.permission.Roles;
import de.kottilabs.todobackend.service.RoleService;
import de.kottilabs.todobackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = { "/api/user" })
@Api(tags = SpringFoxConfig.USER)
public class UserController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@ApiOperation(value = "Find all users", notes = "Accessible with: " + Roles.USER_READ, nickname = "findUsers")
	@Secured(Roles.USER_READ)
	@RequestMapping(method = RequestMethod.GET)
	public List<UserResponse> find() {
		List<User> results = userService.find();
		return results.stream().map(this::convert).collect(Collectors.toList());
	}

	@ApiOperation(value = "Create a user", notes = "Accessible with: " + Roles.USER_CREATE, nickname = "createUser")
	@Secured(Roles.USER_CREATE)
	@RequestMapping(method = RequestMethod.POST)
	public UserResponse create(
			@ApiParam(value = "User to create", required = true) @RequestBody @Validated(UserRequest.Create.class) final UserRequest user) {
		return convert(userService.save(convert(user)));
	}

	@ApiOperation(value = "Update a specific user", notes = "Accessible with: "
			+ Roles.USER_UPDATE, nickname = "updateUser")
	@Secured(Roles.USER_UPDATE)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public UserResponse update(@ApiParam(value = SpringFoxConfig.USER_ID, required = true) @PathVariable final UUID id,
			@ApiParam(value = SpringFoxConfig.UPDATE_REQUEST, required = true) @RequestBody @Validated(UserRequest.Update.class) final UserRequest user) {
		return convert(userService.update(id, convert(user)));
	}

	@ApiOperation(value = "Get a specific user", notes = "Accessible with: " + Roles.USER_READ, nickname = "getUser")
	@Secured(Roles.USER_READ)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public UserResponse get(@ApiParam(value = SpringFoxConfig.USER_ID, required = true) @PathVariable final UUID id) {
		return convert(userService.findById(id));
	}

	@ApiOperation(value = "Get own user", nickname = "getSelf")
	@RequestMapping(value = "/self", method = RequestMethod.GET)
	public UserResponse getSelf(@ApiIgnore Authentication authentication) {
		String username = getUsername(authentication);
		return convert(userService.findByUsername(username));
	}

	@ApiOperation(value = "Update own user", nickname = "updateSelf")
	@RequestMapping(value = "/self", method = RequestMethod.PUT)
	public UserResponse updateSelf(
			@ApiParam(value = SpringFoxConfig.UPDATE_REQUEST, required = true) @RequestBody @Validated(UserRequest.Update.class) final UserRequest user,
			@ApiIgnore Authentication authentication) {
		String username = getUsername(authentication);
		return convert(userService.updateSelf(username, convert(user), user.getPrevPassword()));
	}

	private String getUsername(Authentication authentication) {
		UserDetails principal = (UserDetails) authentication.getPrincipal();
		return principal.getUsername();
	}

	@ApiOperation(value = "Delete a specific user", notes = "Accessible with: "
			+ Roles.USER_CREATE, nickname = "deleteUser")
	@Secured(Roles.USER_CREATE)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public UserResponse delete(
			@ApiParam(value = SpringFoxConfig.USER_ID, required = true) @PathVariable final UUID id) {
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
		if (user.getPassword() != null) {
			mapped.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		mapped.setAuthorities(roleService.findById(user.getAuthorities()));
		return mapped;
	}
}
