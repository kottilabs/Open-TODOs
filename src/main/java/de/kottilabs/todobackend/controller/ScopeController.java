package de.kottilabs.todobackend.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import de.kottilabs.todobackend.dao.Scope;
import de.kottilabs.todobackend.dto.ScopeRequest;
import de.kottilabs.todobackend.dto.ScopeResponse;
import de.kottilabs.todobackend.service.ScopeService;

@RestController
@RequestMapping(value = { "/api/scope" })
public class ScopeController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ScopeService scopeService;

	@RequestMapping(method = RequestMethod.GET)
	private List<ScopeResponse> find() {
		List<Scope> results = scopeService.find();
		System.out.println(results);
		return results.stream().map(this::convert).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.POST)
	private ScopeResponse create(@RequestBody @Validated final ScopeRequest scope) {
		return convert(scopeService.save(convert(scope)));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	private ScopeResponse update(@PathVariable final UUID id, @RequestBody @Validated final ScopeRequest scope) {
		return convert(scopeService.update(id, convert(scope)));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	private ScopeResponse get(@PathVariable final UUID id) {
		return convert(scopeService.findById(id));
	}

	@RequestMapping(value = "/{id}/childs", method = RequestMethod.GET)
	private List<ScopeResponse> findChilds(@PathVariable final UUID id) {
		return scopeService.getScopeWithChildren(id).stream().map(this::convert).collect(Collectors.toList());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	private ScopeResponse delete(@PathVariable final UUID id) {
		return convert(scopeService.delete(id));
	}

	private ScopeResponse convert(Scope scope) {
		ScopeResponse mapped = modelMapper.map(scope, ScopeResponse.class);
		Scope parentScope = scope.getParentScope();
		if (parentScope != null) {
			mapped.setParentScope(parentScope.getId());
		}
		return mapped;
	}

	private Scope convert(ScopeRequest scope) {
		Scope mapped = modelMapper.map(scope, Scope.class);
		UUID parentScope = scope.getParentScope();
		if (parentScope != null) {
			mapped.setParentScope(scopeService.findById(parentScope));
		}
		return mapped;
	}
}
