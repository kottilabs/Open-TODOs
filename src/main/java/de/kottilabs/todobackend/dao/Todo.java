package de.kottilabs.todobackend.dao;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Todo {

	@Id
	@GeneratedValue
	private UUID id;

	@OneToOne
	private Scope scope;

	private String name;

	private TodoState state = TodoState.TODO;

	private String icon;

	private String description;

}
