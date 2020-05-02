package de.kottilabs.todobackend.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ScopeDto {
	private UUID id;

	private String name;

	private UUID parentScope;
}
