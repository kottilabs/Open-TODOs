package de.kottilabs.todobackend.dao;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, UUID> {
	List<Todo> findByScopeIn(Collection<Scope> scopes);

	List<Todo> findByScopeInAndId(Collection<Scope> scopes, UUID id);
}
