package de.kottilabs.todobackend.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;

@Entity
@Data
@IdClass(AuthTokenId.class)
public class AuthToken {

	@Id
	private String username;

	@Id
	private long issuedAt;

	private String password;

	private long validity;
}
