package de.kottilabs.todobackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.kottilabs.todobackend.dao.Scope;
import de.kottilabs.todobackend.dto.TodoDto;
import de.kottilabs.todobackend.service.ScopeService;
import de.kottilabs.todobackend.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import de.kottilabs.todobackend.advice.TodoNotFoundException;
import de.kottilabs.todobackend.dao.Todo;

@RestController
@RequestMapping(value = { "/api/todo" })
public class TodoController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private TodoService todoService;

	@Autowired
	private ScopeService scopeService;

	@RequestMapping(value = "/{scope}", method = RequestMethod.GET)
	private List<TodoDto> find(@PathVariable final UUID scope) {
		List<Todo> result = todoService.find(scope);
		return result.stream().map(this::convert).collect(Collectors.toList());
	}

	@RequestMapping(value = "/{scope}", method = RequestMethod.POST)
	private TodoDto create(@PathVariable final UUID scope, @RequestBody @Validated final TodoDto todo) {
		return convert(todoService.save(convert(todo, scope)));
	}

	@RequestMapping(value = "/{scope}/{id}", method = RequestMethod.GET)
	private TodoDto get(@PathVariable final UUID scope, @PathVariable final UUID id) {
		return convert(todoService.findById(scope, id));
	}

	@RequestMapping(value = "/{scope}/{id}", method = RequestMethod.PUT)
	private TodoDto update(@PathVariable final UUID scope, @PathVariable final UUID id,
			@RequestBody @Validated final TodoDto todo) {
		return convert(todoService.update(scope, id, convert(todo, scope)));
	}

	@RequestMapping(value = "/{scope}/{id}", method = RequestMethod.DELETE)
	private TodoDto delete(@PathVariable final UUID scope, @PathVariable final UUID id) {
		return convert(todoService.delete(scope, id));
	}

	private TodoDto convert(Todo todo) {
		TodoDto mapped = modelMapper.map(todo, TodoDto.class);
		Scope scope = todo.getScope();
		mapped.setScopeId(scope.getId());
		mapped.setScopeName(scope.getName());
		return mapped;
	}

	private Todo convert(TodoDto todo, UUID scope) {
		Todo mapped = modelMapper.map(todo, Todo.class);
		mapped.setScope(scopeService.findById(scope));
		return mapped;
	}
}
