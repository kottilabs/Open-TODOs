package de.kottilabs.todobackend.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Scopes do not match")
public class TodoScopeNotMatchingException extends RuntimeException {
}
