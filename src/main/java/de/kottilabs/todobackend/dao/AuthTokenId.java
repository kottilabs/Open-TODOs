package de.kottilabs.todobackend.dao;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthTokenId implements Serializable {

	private String username;

	private long issuedAt;
}
