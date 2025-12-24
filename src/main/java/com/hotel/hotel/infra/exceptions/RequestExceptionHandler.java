package com.hotel.hotel.infra.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class RequestExceptionHandler {
    
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

    private record Error(String message) {
        public Error(String message) {
            this.message = message;
        }
    }
}
