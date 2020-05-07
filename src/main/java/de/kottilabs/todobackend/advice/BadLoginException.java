package de.kottilabs.todobackend.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid username/password supplied")
public class BadLoginException extends RuntimeException {
}
