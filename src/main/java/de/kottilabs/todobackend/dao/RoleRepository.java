package de.kottilabs.todobackend.dao;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, String> {
	long deleteByRoleNotIn(Collection<String> roles);

	Set<Role> findByRoleIn(Collection<String> roles);
}
