package de.kottilabs.todobackend.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;

import de.kottilabs.todobackend.dao.Role;
import lombok.Data;

@Data
public class UserRequest {

	@NotNull(groups = Create.class)
	private String username;

	private String email;
	@NotNull(groups = Create.class)
	private String password;
	private String prevPassword;

	private String displayname;

	private Set<String> authorities;

	public interface Create {
	}

	public interface Update {
	}
}
