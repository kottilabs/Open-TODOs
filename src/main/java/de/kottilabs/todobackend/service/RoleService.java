package de.kottilabs.todobackend.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.kottilabs.todobackend.advice.RoleNotFoundException;
import de.kottilabs.todobackend.dao.Role;
import de.kottilabs.todobackend.dao.RoleRepository;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;

	public List<Role> find() {
		ArrayList<Role> roles = new ArrayList<>();
		roleRepository.findAll().forEach(roles::add);
		return roles;
	}

	public Role save(Role role) {
		return roleRepository.save(role);
	}

	public Role findById(String role) {
		return roleRepository.findById(role).orElseThrow(RoleNotFoundException::new);
	}

	public Set<Role> findById(Collection<String> roles) {
		if (roles == null) {
			return null;
		}
		return roleRepository.findByRoleIn(roles);
	}

	public Role findByIdOrNull(String role) {
		return roleRepository.findById(role).orElse(null);
	}

	public long deleteNotIn(Collection<String> roles) {
		return roleRepository.deleteByRoleNotIn(roles);
	}
}
