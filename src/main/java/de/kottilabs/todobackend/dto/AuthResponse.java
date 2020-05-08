package de.kottilabs.todobackend.dto;

import java.util.Set;

import lombok.Data;

@Data
public class AuthResponse {
	private String username;
	private String token;
	private Set<String> authorities;
}
