package de.kottilabs.todobackend.dao;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Role {

	@Id
	private String role;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> inherited = new HashSet<>();
}
