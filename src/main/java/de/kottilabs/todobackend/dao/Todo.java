package de.kottilabs.todobackend.dao;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Todo {

	@Id
	@GeneratedValue
	private UUID id;

	private String name;
	private String description;
}
