package de.kottilabs.todobackend.dao;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Scope {

	@Id
	@GeneratedValue
	private UUID id;

	private String name;

	@OneToOne
	private Scope parentScope;
}
