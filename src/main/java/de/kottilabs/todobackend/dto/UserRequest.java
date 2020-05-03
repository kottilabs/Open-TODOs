package de.kottilabs.todobackend.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;

import de.kottilabs.todobackend.dao.Role;
import lombok.Data;

@Data
public class UserRequest {

	@NotNull
	private String username;

	private String email;
	@NotNull
	private String password;
	private String prevPassword;

	private String displayname;

	private Set<String> authorities;
}
