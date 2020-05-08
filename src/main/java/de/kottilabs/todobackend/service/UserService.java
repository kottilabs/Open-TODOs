package de.kottilabs.todobackend.service;

import java.util.*;

import de.kottilabs.todobackend.advice.*;
import de.kottilabs.todobackend.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

	public User updateSelf(UUID id, User user) {
		User userInDB = findById(id);
		return updateUser(userInDB, user);
	}

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

	private User updateUser(User userInDB, User user) {
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
		if (user.getAuthorities() != null) {
			userInDB.setAuthorities(user.getAuthorities());
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
