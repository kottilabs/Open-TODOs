package de.kottilabs.todobackend.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import de.kottilabs.todobackend.dao.Scope;
import de.kottilabs.todobackend.dto.ScopeDto;
import de.kottilabs.todobackend.dto.TodoDto;
import de.kottilabs.todobackend.service.ScopeService;

@RestController
@RequestMapping(value = { "/api/scope" })
public class ScopeController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ScopeService scopeService;

	@RequestMapping(method = RequestMethod.GET)
	private List<ScopeDto> find() {
		List<Scope> result = scopeService.find();
		return result.stream().map(this::convert).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.POST)
	private ScopeDto create(@RequestBody @Validated final ScopeDto scope) {
		return convert(scopeService.save(convert(scope)));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	private ScopeDto update(@PathVariable final UUID id, @RequestBody @Validated final ScopeDto scope) {
		return convert(scopeService.update(id, convert(scope)));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	private ScopeDto get(@PathVariable final UUID id) {
		return convert(scopeService.findById(id));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	private ScopeDto delete(@PathVariable final UUID id) {
		return convert(scopeService.delete(id));
	}

	private ScopeDto convert(Scope scope) {
		ScopeDto mapped = modelMapper.map(scope, ScopeDto.class);
		Scope parentScope = scope.getParentScope();
		if (parentScope != null) {
			mapped.setParentScope(parentScope.getId());
		}
		return mapped;
	}

	private Scope convert(ScopeDto scope) {
		Scope mapped = modelMapper.map(scope, Scope.class);
		UUID parentScope = scope.getParentScope();
		if (parentScope != null) {
			mapped.setParentScope(scopeService.findById(parentScope));
		}
		return mapped;
	}
}
