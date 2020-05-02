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
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TodoResponse extends TodoRequest {

	private UUID id;

	private String scopeName;
}
