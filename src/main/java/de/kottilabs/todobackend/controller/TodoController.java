package de.kottilabs.todobackend.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.kottilabs.todobackend.permission.Roles;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import de.kottilabs.todobackend.advice.TodoScopeNotMatchingException;
import de.kottilabs.todobackend.dao.Scope;
import de.kottilabs.todobackend.dao.Todo;
import de.kottilabs.todobackend.dto.TodoRequest;
import de.kottilabs.todobackend.dto.TodoResponse;
import de.kottilabs.todobackend.service.ScopeService;
import de.kottilabs.todobackend.service.TodoService;

@RestController
@RequestMapping(value = { "/api/todo" })
public class TodoController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private TodoService todoService;

	@Autowired
	private ScopeService scopeService;

	@Secured(Roles.TODO_READ)
	@RequestMapping(value = "/{scope}", method = RequestMethod.GET)
	private List<TodoResponse> find(@PathVariable final UUID scope) {
		List<Todo> results = todoService.find(scope);
		return results.stream().map(this::convert).collect(Collectors.toList());
	}

	@Secured(Roles.TODO_CREATE)
	@RequestMapping(value = "/{scope}", method = RequestMethod.POST)
	private TodoResponse create(@PathVariable final UUID scope,
			@RequestBody @Validated(TodoRequest.Create.class) final TodoRequest todo) {
		UUID scopeId = todo.getScopeId();
		if (scopeId != null) {
			if (!scopeId.equals(scope)) {
				throw new TodoScopeNotMatchingException();
			}
		}
		return convert(todoService.save(convert(todo, scope)));
	}

	@Secured(Roles.TODO_UPDATE)
	@RequestMapping(value = "/{scope}/{id}", method = RequestMethod.PUT)
	private TodoResponse update(@PathVariable final UUID scope, @PathVariable final UUID id,
			@RequestBody @Validated(TodoRequest.Update.class) final TodoRequest todo) {
		return convert(todoService.update(scope, id, convert(todo, todo.getScopeId())));
	}

	@Secured(Roles.TODO_READ)
	@RequestMapping(value = "/{scope}/{id}", method = RequestMethod.GET)
	private TodoResponse get(@PathVariable final UUID scope, @PathVariable final UUID id) {
		return convert(todoService.findById(scope, id));
	}

	@Secured(Roles.TODO_CREATE)
	@RequestMapping(value = "/{scope}/{id}", method = RequestMethod.DELETE)
	private TodoResponse delete(@PathVariable final UUID scope, @PathVariable final UUID id) {
		return convert(todoService.delete(scope, id));
	}

	private TodoResponse convert(Todo todo) {
		TodoResponse mapped = modelMapper.map(todo, TodoResponse.class);
		Scope scope = todo.getScope();
		mapped.setScopeId(scope.getId());
		mapped.setScopeName(scope.getName());
		return mapped;
	}

	private Todo convert(TodoRequest todo, UUID scope) {
		Todo mapped = modelMapper.map(todo, Todo.class);
		mapped.setScope(scopeService.findById(scope));
		return mapped;
	}
}
