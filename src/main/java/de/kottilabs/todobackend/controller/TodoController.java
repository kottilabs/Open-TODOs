package de.kottilabs.todobackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import de.kottilabs.todobackend.advice.TodoNotFoundException;
import de.kottilabs.todobackend.dao.Todo;
import de.kottilabs.todobackend.dao.TodoRepository;

@RestController
@RequestMapping(value = { "/api/todo" })
public class TodoController {

	@Autowired
	private TodoRepository repository;

	@GetMapping
	private List<Todo> find() {
		List<Todo> result = new ArrayList<>();
		repository.findAll().forEach(result::add);
		return result;
	}

	@PostMapping
	private Todo create(@RequestBody final Todo todo) {
		return repository.save(todo);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	private Todo get(@PathVariable final UUID id) {
		return repository.findById(id).orElseThrow(() -> new TodoNotFoundException());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	private void delete(@PathVariable final UUID id) {
		repository.findById(id).orElseThrow(() -> new TodoNotFoundException());
		repository.deleteById(id);
	}
}
