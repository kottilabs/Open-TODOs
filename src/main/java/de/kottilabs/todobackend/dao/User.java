package de.kottilabs.todobackend.dao;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "password")
public class User {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(unique = true, nullable = false)
	private String username;
	@Column(unique = true)
	private String email;
	private String password;

	private String displayname;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Role> authorities = new HashSet<>();
}
