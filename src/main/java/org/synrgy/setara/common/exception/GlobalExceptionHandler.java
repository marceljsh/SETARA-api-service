package org.synrgy.setara.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.synrgy.setara.common.utils.GenericResponse;
import jakarta.persistence.EntityNotFoundException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<GenericResponse<Void>> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error("Entity not found exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource not found");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GenericResponse<Void>> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        log.error("Authentication exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GenericResponse<Void>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.error("Access denied exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Access denied");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Void>> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Internal server error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred");
    }

    private ResponseEntity<GenericResponse<Void>> buildErrorResponse(HttpStatus status, String message) {
        GenericResponse<Void> response = GenericResponse.error(status, message);
        return new ResponseEntity<>(response, status);
    }
}
