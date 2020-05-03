package de.kottilabs.todobackend.service;

import java.util.*;

import de.kottilabs.todobackend.advice.UserNotFoundException;
import de.kottilabs.todobackend.dao.Role;
import de.kottilabs.todobackend.dao.User;
import de.kottilabs.todobackend.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.kottilabs.todobackend.advice.ScopeNotFoundException;
import de.kottilabs.todobackend.dao.Scope;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public List<User> find() {
		ArrayList<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		return users;
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public User update(UUID id, User user) {
		User userInDB = findById(id);
		return updateUser(userInDB, user);
	}

	public User update(String username, User user) {
		User userInDB = findByUsername(username);
		return updateUser(userInDB, user);
	}

	private User updateUser(User userInDB, User user) {
		userInDB.setUsername(user.getUsername());
		userInDB.setPassword(user.getPassword());
		userInDB.setDisplayname(user.getDisplayname());
		userInDB.setEmail(user.getEmail());

		return userRepository.save(user);
	}

	public User findById(UUID id) {
		return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
	}

	public User findByUsernameOrNull(String role) {
		return userRepository.findByUsername(role).orElse(null);
	}

	public User delete(UUID id) {
		User user = findById(id);
		userRepository.delete(user);
		return user;
	}
}
