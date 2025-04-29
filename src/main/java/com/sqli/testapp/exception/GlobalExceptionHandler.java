package com.sqli.testapp.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, ERROR);
        response.put(MESSAGE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, ERROR);
        response.put(MESSAGE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, ERROR);
        response.put(MESSAGE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
