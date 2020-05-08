package de.kottilabs.todobackend.dto;

import java.util.UUID;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ScopeRequest {

	@NotNull(groups = Create.class)
	private String name;

	private UUID parentScope;

	public interface Create {
	}

	public interface Update {
	}
}
