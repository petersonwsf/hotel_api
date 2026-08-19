package com.hotel.hotel.config.exceptions;

import com.hotel.hotel.modules.audit.AuditEvent;
import com.hotel.hotel.modules.audit.AuditOutcome;
import com.hotel.hotel.modules.audit.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

@RestControllerAdvice
public class RequestExceptionHandler {

    @Autowired
    private AuditService auditService;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)  // 401
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(AccessResourceDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessResourceDeniedException exception, HttpServletRequest request) {
        auditService.record(AuditEvent.builder()
                .action("ACCESS_DENIED")
                .actor(getCurrentUser())
                .actorIp(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .extraData(request.getRequestURI())
                .outcome(AuditOutcome.FAILURE)
                .errorMessage(exception.getMessage())
                .build());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(exception.getMessage()));
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
        return ResponseEntity.status(409).body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler(RoomNotAvailable.class)
    public ResponseEntity roomNotAvailable(RoomNotAvailable error) {
        return ResponseEntity.status(409).body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity resourceNotFound(ResourceNotFoundException error) {
        return ResponseEntity.status(404).body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler(MyCustomStorageException.class)
    public ResponseEntity minioError(MyCustomStorageException error) {
        return ResponseEntity.status(500).body(new ErrorResponse(error.getMessage()));
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

    private String getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElse("anonymous");
    }
}
