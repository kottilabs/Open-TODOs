package de.kottilabs.todobackend.dto;

import javax.validation.constraints.NotNull;

import de.kottilabs.todobackend.dao.TodoState;
import lombok.Data;

@Data
public class TodoRequest {

	@NotNull
	private String name;

	private TodoState state = TodoState.TODO;

	@NotNull
	private String description;
}
