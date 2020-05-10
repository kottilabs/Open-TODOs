package de.kottilabs.todobackend.dto;

import java.util.Set;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "Login response")
@Data
public class AuthResponse {

	@ApiModelProperty(notes = "Your username")
	private String username;

	@ApiModelProperty(notes = "Your API Bearer token")
	private String token;

	@ApiModelProperty(notes = "Your authorities")
	private Set<String> authorities;

}
