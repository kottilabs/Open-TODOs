package de.kottilabs.todobackend.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Previous password is wrong")
public class PreviousPasswordInvalidException extends RuntimeException {
}
