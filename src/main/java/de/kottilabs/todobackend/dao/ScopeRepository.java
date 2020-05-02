package de.kottilabs.todobackend.dao;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface ScopeRepository extends CrudRepository<Scope, UUID> {
	List<Scope> findByParentScopeIn(Collection<Scope> scopes);
}
