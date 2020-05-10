package de.kottilabs.todobackend.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import de.kottilabs.todobackend.config.SpringFoxConfig;
import de.kottilabs.todobackend.dao.Scope;
import de.kottilabs.todobackend.dto.ScopeRequest;
import de.kottilabs.todobackend.dto.ScopeResponse;
import de.kottilabs.todobackend.permission.Roles;
import de.kottilabs.todobackend.service.ScopeService;

@RestController
@RequestMapping(value = { "/api/scope" })
@Api(tags = SpringFoxConfig.SCOPE)
public class ScopeController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ScopeService scopeService;

	@ApiOperation(value = "Find all scopes", notes = "Accessible with: " + Roles.SCOPE_READ, nickname = "findScopes")
	@Secured(Roles.SCOPE_READ)
	@RequestMapping(method = RequestMethod.GET)
	public List<ScopeResponse> find() {
		List<Scope> results = scopeService.find();
		return results.stream().map(this::convert).collect(Collectors.toList());
	}

	@ApiOperation(value = "Create a scope", notes = "Accessible with: " + Roles.SCOPE_CREATE, nickname = "createScope")
	@Secured(Roles.SCOPE_CREATE)
	@RequestMapping(method = RequestMethod.POST)
	public ScopeResponse create(
			@ApiParam(value = "Scope to create", required = true, type = "ScopeRequest") @RequestBody @Validated(ScopeRequest.Create.class) final ScopeRequest scope) {
		return convert(scopeService.save(convert(scope)));
	}

	@ApiOperation(value = "Update a specific scope", notes = "Accessible with: "
			+ Roles.SCOPE_UPDATE, nickname = "updateScope")
	@Secured(Roles.SCOPE_UPDATE)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ScopeResponse update(
			@ApiParam(value = SpringFoxConfig.SCOPE_ID, required = true) @PathVariable final UUID id,
			@ApiParam(value = SpringFoxConfig.UPDATE_REQUEST, required = true, type = "ScopeRequest") @RequestBody @Validated(ScopeRequest.Update.class) final ScopeRequest scope) {
		return convert(scopeService.update(id, convert(scope)));
	}

	@ApiOperation(value = "Get a specific scope", notes = "Accessible with: " + Roles.SCOPE_READ, nickname = "getScope")
	@Secured(Roles.SCOPE_READ)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ScopeResponse get(@ApiParam(value = SpringFoxConfig.SCOPE_ID, required = true) @PathVariable final UUID id) {
		return convert(scopeService.findById(id));
	}

	@ApiOperation(value = "Find all children of specific scope", notes = "Accessible with: "
			+ Roles.SCOPE_READ, nickname = "getChildScopes")
	@Secured(Roles.SCOPE_READ)
	@RequestMapping(value = "/{id}/childs", method = RequestMethod.GET)
	public List<ScopeResponse> findChilds(
			@ApiParam(value = SpringFoxConfig.SCOPE_ID, required = true) @PathVariable final UUID id) {
		return scopeService.getScopeWithChildren(id).stream().map(this::convert).collect(Collectors.toList());
	}

	@ApiOperation(value = "Delete a specific scope", notes = "Accessible with: "
			+ Roles.SCOPE_CREATE, nickname = "deleteScope")
	@Secured(Roles.SCOPE_CREATE)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ScopeResponse delete(
			@ApiParam(value = SpringFoxConfig.SCOPE_ID, required = true) @PathVariable final UUID id) {
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
