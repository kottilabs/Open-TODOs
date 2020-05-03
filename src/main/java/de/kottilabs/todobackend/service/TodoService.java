package de.kottilabs.todobackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import de.kottilabs.todobackend.dao.Scope;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.kottilabs.todobackend.advice.TodoNotFoundException;
import de.kottilabs.todobackend.dao.Todo;
import de.kottilabs.todobackend.dao.TodoRepository;

@Service
public class TodoService {
	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private ScopeService scopeService;

	public List<Todo> find(UUID scope) {
		Set<Scope> scopeWithChildren = scopeService.getScopeWithChildren(scope);

		ArrayList<Todo> todos = new ArrayList<>();
		todoRepository.findByScopeIn(scopeWithChildren).forEach(todos::add);
		return todos;
	}

	public Todo save(Todo todo) {
		return todoRepository.save(todo);
	}

	public Todo update(UUID scope, UUID id, Todo todo) {
		Todo todoInDB = findById(scope, id);

		todoInDB.setName(todo.getName());
		todoInDB.setDescription(todo.getDescription());
		todoInDB.setScope(todo.getScope());
		todoInDB.setState(todo.getState());
		todoInDB.setIcon(todo.getIcon());

		return todoRepository.save(todoInDB);
	}

	public Todo findById(UUID scope, UUID id) {
		Set<Scope> scopeWithChildren = scopeService.getScopeWithChildren(scope);

		List<Todo> byScopeInAndId = todoRepository.findByScopeInAndId(scopeWithChildren, id);
		if (byScopeInAndId.isEmpty()) {
			throw new TodoNotFoundException();
		}
		if (byScopeInAndId.size() > 1) {
			// this should never happen just to be safe
			throw new IllegalStateException("There are multiple todos with the same id: " + byScopeInAndId);
		}

		return byScopeInAndId.get(0);
	}

	public Todo delete(UUID scope, UUID id) {
		Todo todo = findById(scope, id);
		todoRepository.delete(todo);
		return todo;
	}
}
