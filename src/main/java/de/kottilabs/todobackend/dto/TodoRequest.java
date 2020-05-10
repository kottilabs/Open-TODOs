package de.kottilabs.todobackend.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import de.kottilabs.todobackend.config.SpringFoxConfig;
import de.kottilabs.todobackend.dao.TodoState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.UUID;

@ApiModel(description = "Todo request")
@Data
public class TodoRequest {

	@ApiModelProperty(notes = "The todos name")
	@NotNull(groups = Create.class)
	private String name;

	@ApiModelProperty(notes = "The todos state")
	@NotNull(groups = Create.class)
	private TodoState state;

	@ApiModelProperty(notes = "The todos scope id")
	@NotNull(groups = Update.class)
	private UUID scopeId;

	@ApiModelProperty(notes = "The todos description")
	@NotNull(groups = Create.class)
	private String description;

	@ApiModelProperty(notes = "The todos icon")
	private String icon;

	public interface Create {
	}

	public interface Update {
	}
}
