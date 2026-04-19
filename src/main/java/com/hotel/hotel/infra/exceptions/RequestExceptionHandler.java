package com.hotel.hotel.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)  // 401
                .body(new ErrorResponse("Invalid credentials"));
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity notFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity badRequest(MethodArgumentNotValidException error) {
        var errors = error.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(ErrorData::new).toList());
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity alreadyExists(ResourceAlreadyExists error) {
        return ResponseEntity.status(409).body(new Error(error.getMessage()));
    }

    @ExceptionHandler(RoomNotAvailable.class)
    public ResponseEntity roomNotAvailable(RoomNotAvailable error) {
        return ResponseEntity.status(409).body(new Error(error.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity alreadyExists(ResourceNotFoundException error) {
        return ResponseEntity.status(404).body(new Error(error.getMessage()));
    }

    private record ErrorData(String field, String error) {
        public ErrorData(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    private record ErrorResponse(String message) {
        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
