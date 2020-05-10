package de.kottilabs.todobackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "Logout response")
@Data
public class AuthLogoutResponse {

	@ApiModelProperty(notes = "Amount of tokens invalidated")
	private long tokensInvalidated;

}
