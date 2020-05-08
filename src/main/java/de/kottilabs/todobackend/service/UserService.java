package de.kottilabs.todobackend.service;

import java.util.*;

import de.kottilabs.todobackend.advice.*;
import de.kottilabs.todobackend.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthTokenRepository authTokenRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> find() {
		ArrayList<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		return users;
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	@Transactional
	public User update(UUID id, User user) {
		User userInDB = findById(id);
		return updateUser(userInDB, user);
	}

	@Transactional
	public User updateSelf(String username, User user, String prevPassword) {
		User userInDB = findByUsername(username);

		if (user.getPassword() != null) {
			if (prevPassword == null) {
				throw new PreviousPasswordMustBeSetException();
			}

			if (!passwordEncoder.matches(prevPassword, userInDB.getPassword())) {
				throw new PreviousPasswordInvalidException();
			}
		}

		if (user.getAuthorities() != null) {
			throw new NotAllowedToSetAuthoritiesException();
		}

		return updateUser(userInDB, user);
	}

	@Transactional
	public long deleteByAuthToken(String username, long issuedAt) {
		return authTokenRepository.deleteByUsernameAndIssuedAt(username, issuedAt);
	}

	@Transactional
	public long deleteByUsername(String username) {
		return authTokenRepository.deleteByUsername(username);
	}

	private User updateUser(User userInDB, User user) {
		if (user.getAuthorities() != null) {
			authTokenRepository.deleteByUsername(userInDB.getUsername());
			userInDB.setAuthorities(user.getAuthorities());
		}
		if (user.getUsername() != null) {
			userInDB.setUsername(user.getUsername());
		}
		if (user.getPassword() != null) {
			userInDB.setPassword(user.getPassword());
		}
		if (user.getDisplayname() != null) {
			userInDB.setDisplayname(user.getDisplayname());
		}
		if (user.getEmail() != null) {
			userInDB.setEmail(user.getEmail());
		}

		return userRepository.save(userInDB);
	}

	public User findById(UUID id) {
		return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
	}

	public User findByUsernameOrNull(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	public User delete(UUID id) {
		User user = findById(id);
		userRepository.delete(user);
		return user;
	}
}
