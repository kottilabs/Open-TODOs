package de.kottilabs.todobackend.dto;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import de.kottilabs.todobackend.dao.Scope;
import de.kottilabs.todobackend.dao.TodoState;
import lombok.Data;

@Data
public class TodoDto {

	private UUID id;

	@NotNull
	private String name;

	private TodoState state = TodoState.TODO;

	@NotNull
	private String description;

	private UUID scopeId;
	
	private String scopeName;
}
