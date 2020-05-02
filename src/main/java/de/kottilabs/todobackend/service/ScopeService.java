package de.kottilabs.todobackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.kottilabs.todobackend.advice.ScopeNotFoundException;
import de.kottilabs.todobackend.dao.Scope;
import de.kottilabs.todobackend.dao.ScopeRepository;

@Service
public class ScopeService {
	@Autowired
	private ScopeRepository scopeRepository;

	public List<Scope> find() {
		ArrayList<Scope> scopes = new ArrayList<>();
		scopeRepository.findAll().forEach(scopes::add);
		return scopes;
	}

	public Scope save(Scope scope) {
		return scopeRepository.save(scope);
	}

	public Scope update(UUID id, Scope scope) {
		Scope scopeInDB = findById(id);

		Scope parentScope = scope.getParentScope();
		if (parentScope != null) {
			scopeInDB.setParentScope(parentScope);
		}

		String name = scope.getName();
		if (name != null) {
			scopeInDB.setName(name);
		}

		return scopeRepository.save(scope);
	}

	public Scope findById(UUID id) {
		return scopeRepository.findById(id).orElseThrow(ScopeNotFoundException::new);
	}

	public Scope delete(UUID id) {
		Scope scope = scopeRepository.findById(id).orElseThrow(ScopeNotFoundException::new);
		scopeRepository.delete(scope);
		return scope;
	}
}
