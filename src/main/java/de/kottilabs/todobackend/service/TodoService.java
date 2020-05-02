package de.kottilabs.todobackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.kottilabs.todobackend.advice.TodoNotFoundException;
import de.kottilabs.todobackend.dao.Todo;
import de.kottilabs.todobackend.dao.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.kottilabs.todobackend.advice.ScopeNotFoundException;
import de.kottilabs.todobackend.dao.Scope;
import de.kottilabs.todobackend.dao.ScopeRepository;

@Service
public class TodoService {
	@Autowired
	private TodoRepository todoRepository;

	public List<Todo> find(UUID scope) {
		ArrayList<Todo> todos = new ArrayList<>();
		todoRepository.findAll().forEach(todos::add);
		return todos;
	}

	public Todo save(Todo todo) {
		return todoRepository.save(todo);
	}

	public Todo update(UUID scope, UUID id, Todo todo) {
		Todo todoInDB = findById(scope, id);

		String name = todo.getName();
		if (name != null) {
			todoInDB.setName(name);
		}

		todoInDB.setScope(todo.getScope());

		return todoRepository.save(todoInDB);
	}

	public Todo findById(UUID scope, UUID id) {
		return todoRepository.findById(id).orElseThrow(TodoNotFoundException::new);
	}

	public Todo delete(UUID scope, UUID id) {
		Todo todo = todoRepository.findById(id).orElseThrow(TodoNotFoundException::new);
		todoRepository.delete(todo);
		return todo;
	}
}
