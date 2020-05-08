package de.kottilabs.todobackend.dto;

import lombok.Data;

@Data
public class AuthLogoutResponse {
	private long tokensInvalidated;
}
