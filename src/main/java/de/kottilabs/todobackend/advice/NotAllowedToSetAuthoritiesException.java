package de.kottilabs.todobackend.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "You are not allowed to set your own authorities")
public class NotAllowedToSetAuthoritiesException extends RuntimeException {
}
