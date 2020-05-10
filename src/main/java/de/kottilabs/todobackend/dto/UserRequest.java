package de.kottilabs.todobackend.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;

import de.kottilabs.todobackend.dao.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "User request")
@Data
public class UserRequest {

	@ApiModelProperty(notes = "The users username")
	@NotNull(groups = Create.class)
	private String username;

	@ApiModelProperty(notes = "The users email")
	private String email;

	@ApiModelProperty(notes = "The users password")
	@NotNull(groups = Create.class)
	private String password;

	@ApiModelProperty(notes = "The users previous password")
	private String prevPassword;

	@ApiModelProperty(notes = "The users displayname")
	private String displayname;

	@ApiModelProperty(notes = "The users authorities")
	private Set<String> authorities;

	public interface Create {
	}

	public interface Update {
	}
}
