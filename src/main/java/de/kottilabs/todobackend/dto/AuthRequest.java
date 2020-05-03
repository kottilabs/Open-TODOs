package de.kottilabs.todobackend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthRequest {
	@NotNull
	private String username;
	@NotNull
	private String password;
}
