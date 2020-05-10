package de.kottilabs.todobackend.dto;

import de.kottilabs.todobackend.config.SpringFoxConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@ApiModel(description = "Scope response")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScopeResponse extends ScopeRequest {

	@ApiModelProperty(notes = SpringFoxConfig.SCOPE_ID)
	private UUID id;

}
