package de.kottilabs.todobackend.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ScopeRequest {

	private String name;

	private UUID parentScope;
}
