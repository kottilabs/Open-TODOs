package de.kottilabs.todobackend.dao;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface AuthTokenRepository extends CrudRepository<AuthToken, String> {
	Optional<AuthToken> findByUsernameAndValidityGreaterThan(String username, long now);

	long deleteByValidityLessThan(long now);
}
