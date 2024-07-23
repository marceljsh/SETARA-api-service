//package org.synrgy.setara.common.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.synrgy.setara.common.dto.GenericResponse;
//
//@ControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<GenericResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
//        log.error("Access denied: {}", ex.getMessage(), ex);
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .body(GenericResponse.error(HttpStatus.FORBIDDEN, "Access denied"));
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<GenericResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
//        log.error("Bad credentials: {}", ex.getMessage(), ex);
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(GenericResponse.error(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<GenericResponse<Void>> handleRuntimeException(RuntimeException ex) {
//        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        String message = ex.getMessage() != null ? ex.getMessage() : "An error occurred";
//        return ResponseEntity.status(status)
//                .body(GenericResponse.error(status, message));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<GenericResponse<Void>> handleException(Exception ex) {
//        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(GenericResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"));
//    }
//}
