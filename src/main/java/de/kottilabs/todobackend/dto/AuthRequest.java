package de.kottilabs.todobackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Login credentials")
@Data
public class AuthRequest {

	@ApiModelProperty(notes = "The username")
	@NotNull
	private String username;

	@ApiModelProperty(notes = "The password")
	@NotNull
	private String password;

}
