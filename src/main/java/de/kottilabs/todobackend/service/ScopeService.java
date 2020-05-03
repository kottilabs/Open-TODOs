package de.kottilabs.todobackend.service;

import java.util.*;

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

		scopeInDB.setName(scope.getName());
		scopeInDB.setParentScope(scope.getParentScope());

		return scopeRepository.save(scope);
	}

	public Scope findById(UUID id) {
		return scopeRepository.findById(id).orElseThrow(ScopeNotFoundException::new);
	}

	public Scope delete(UUID id) {
		Scope scope = findById(id);
		scopeRepository.delete(scope);
		return scope;
	}

	public Set<Scope> getScopeWithChildren(UUID id) {
		Set<Scope> scopes = new HashSet<>();
		Scope scope = findById(id);
		scopes.add(scope);

		List<Scope> byParentScopeIn = scopeRepository.findByParentScopeIn(Collections.singletonList(scope));
		recursiveAdd(scopes, byParentScopeIn);

		return scopes;
	}

	private void recursiveAdd(Set<Scope> scopes, List<Scope> byParentScopeIn) {
		if (!byParentScopeIn.isEmpty()) {
			scopes.addAll(byParentScopeIn);
			recursiveAdd(scopes, scopeRepository.findByParentScopeIn(byParentScopeIn));
		}
	}
}
