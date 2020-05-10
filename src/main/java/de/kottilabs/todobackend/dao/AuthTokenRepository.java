package de.kottilabs.todobackend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AuthTokenRepository extends CrudRepository<AuthToken, String> {
	Optional<AuthToken> findByUsernameAndIssuedAtAndValidityGreaterThan(String username, long issuedAt, long now);

	long deleteByValidityLessThan(long now);

	long deleteByUsername(String username);

	long deleteByUsernameAndIssuedAt(String username, long issuedAt);

	@Query("Select count(distinct username) from AuthToken")
	long countDistinctUsername();
}
