package de.kottilabs.todobackend.dao;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class AuthToken {

	@Id
	private String username;

	private long validity;
}
