package de.kottilabs.todobackend.dao;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TodoRepository extends CrudRepository<Todo, UUID> {
}
