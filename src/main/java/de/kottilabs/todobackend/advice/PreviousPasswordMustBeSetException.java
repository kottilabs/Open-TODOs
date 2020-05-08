package de.kottilabs.todobackend.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Previous password must be set")
public class PreviousPasswordMustBeSetException extends RuntimeException {
}
