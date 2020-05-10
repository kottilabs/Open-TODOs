package de.kottilabs.todobackend.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import de.kottilabs.todobackend.advice.TodoScopeNotMatchingException;
import de.kottilabs.todobackend.config.SpringFoxConfig;
import de.kottilabs.todobackend.dao.Scope;
import de.kottilabs.todobackend.dao.Todo;
import de.kottilabs.todobackend.dto.TodoRequest;
import de.kottilabs.todobackend.dto.TodoResponse;
import de.kottilabs.todobackend.permission.Roles;
import de.kottilabs.todobackend.service.ScopeService;
import de.kottilabs.todobackend.service.TodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = { "/api/todo" })
@Api(tags = SpringFoxConfig.TODO)
public class TodoController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private TodoService todoService;

	@Autowired
	private ScopeService scopeService;

	@ApiOperation(value = "Find all todos in scope", notes = "Accessible with: " + Roles.TODO_READ)
	@Secured(Roles.TODO_READ)
	@RequestMapping(value = "/{scope}", method = RequestMethod.GET)
	public List<TodoResponse> find(
			@ApiParam(value = SpringFoxConfig.SCOPE_ID, required = true) @PathVariable final UUID scope) {
		List<Todo> results = todoService.find(scope);
		return results.stream().map(this::convert).collect(Collectors.toList());
	}

	@ApiOperation(value = "Create a todo in scope", notes = "Accessible with: " + Roles.TODO_CREATE)
	@Secured(Roles.TODO_CREATE)
	@RequestMapping(value = "/{scope}", method = RequestMethod.POST)
	public TodoResponse create(
			@ApiParam(value = SpringFoxConfig.SCOPE_ID, required = true) @PathVariable final UUID scope,
			@ApiParam(value = "Todo to Create", required = true) @RequestBody @Validated(TodoRequest.Create.class) final TodoRequest todo) {
		UUID scopeId = todo.getScopeId();
		if (scopeId != null) {
			if (!scopeId.equals(scope)) {
				throw new TodoScopeNotMatchingException();
			}
		}
		return convert(todoService.save(convert(todo, scope)));
	}

	@ApiOperation(value = "Update specific todo in scope", notes = "Accessible with: " + Roles.TODO_UPDATE)
	@Secured(Roles.TODO_UPDATE)
	@RequestMapping(value = "/{scope}/{id}", method = RequestMethod.PUT)
	public TodoResponse update(
			@ApiParam(value = SpringFoxConfig.SCOPE_ID, required = true) @PathVariable final UUID scope,
			@ApiParam(value = SpringFoxConfig.TODO_ID, required = true) @PathVariable final UUID id,
			@ApiParam(value = SpringFoxConfig.UPDATE_REQUEST, required = true) @RequestBody @Validated(TodoRequest.Update.class) final TodoRequest todo) {
		return convert(todoService.update(scope, id, convert(todo, todo.getScopeId())));
	}

	@ApiOperation(value = "Get specific todo in scope", notes = "Accessible with: " + Roles.TODO_READ)
	@Secured(Roles.TODO_READ)
	@RequestMapping(value = "/{scope}/{id}", method = RequestMethod.GET)
	public TodoResponse get(@ApiParam(value = SpringFoxConfig.SCOPE_ID, required = true) @PathVariable final UUID scope,
			@ApiParam(value = SpringFoxConfig.TODO_ID, required = true) @PathVariable final UUID id) {
		return convert(todoService.findById(scope, id));
	}

	@ApiOperation(value = "Delete specific todo in scope", notes = "Accessible with: " + Roles.TODO_CREATE)
	@Secured(Roles.TODO_CREATE)
	@RequestMapping(value = "/{scope}/{id}", method = RequestMethod.DELETE)
	public TodoResponse delete(
			@ApiParam(value = SpringFoxConfig.SCOPE_ID, required = true) @PathVariable final UUID scope,
			@ApiParam(value = SpringFoxConfig.TODO_ID, required = true) @PathVariable final UUID id) {
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
