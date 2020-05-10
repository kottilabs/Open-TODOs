package de.kottilabs.todobackend.dto;

import java.util.UUID;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Scope request")
@Data
public class ScopeRequest {

	@ApiModelProperty(notes = "The scopes name")
	@NotNull(groups = Create.class)
	private String name;

	@ApiModelProperty(notes = "The scopes parent")
	private UUID parentScope;

	public interface Create {
	}

	public interface Update {
	}

}
