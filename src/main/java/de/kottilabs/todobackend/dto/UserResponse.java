package de.kottilabs.todobackend.dto;

import java.util.UUID;

import de.kottilabs.todobackend.config.SpringFoxConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "User response")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends UserRequest {

	@ApiModelProperty(notes = SpringFoxConfig.USER_ID)
	private UUID id;

}
