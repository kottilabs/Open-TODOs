package de.kottilabs.todobackend.dto;

import java.util.UUID;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ScopeRequest {

	@NotNull
	private String name;

	private UUID parentScope;
}
