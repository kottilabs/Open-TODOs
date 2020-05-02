package de.kottilabs.todobackend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScopeResponse extends ScopeRequest {
	private UUID id;
}
