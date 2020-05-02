package de.kottilabs.todobackend.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface ScopeRepository extends CrudRepository<Scope, UUID> {
}
