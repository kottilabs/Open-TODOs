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

	@Secured(Roles.SCOPE_READ)
	@RequestMapping(method = RequestMethod.GET)
	public List<ScopeResponse> find() {
		List<Scope> results = scopeService.find();
		return results.stream().map(this::convert).collect(Collectors.toList());
	}

	@Secured(Roles.SCOPE_CREATE)
	@RequestMapping(method = RequestMethod.POST)
	public ScopeResponse create(@RequestBody @Validated final ScopeRequest scope) {
		return convert(scopeService.save(convert(scope)));
	}

	@Secured(Roles.SCOPE_UPDATE)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ScopeResponse update(@PathVariable final UUID id, @RequestBody @Validated final ScopeRequest scope) {
		return convert(scopeService.update(id, convert(scope)));
	}

	@Secured(Roles.SCOPE_READ)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ScopeResponse get(@PathVariable final UUID id) {
		return convert(scopeService.findById(id));
	}

	@Secured(Roles.SCOPE_READ)
	@RequestMapping(value = "/{id}/childs", method = RequestMethod.GET)
	public List<ScopeResponse> findChilds(@PathVariable final UUID id) {
		return scopeService.getScopeWithChildren(id).stream().map(this::convert).collect(Collectors.toList());
	}

	@Secured(Roles.SCOPE_CREATE)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ScopeResponse delete(@PathVariable final UUID id) {
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
