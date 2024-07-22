package org.synrgy.setara.transaction.advice;

import org.synrgy.setara.transaction.exception.TransactionExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class TransactionAdviceExceptionHandler {

    @ExceptionHandler(TransactionExceptions.InvalidMpinException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidMpinException(TransactionExceptions.InvalidMpinException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST
        ));
    }

    @ExceptionHandler(TransactionExceptions.InsufficientBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientBalanceException(TransactionExceptions.InsufficientBalanceException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST
        ));
    }

    @ExceptionHandler(TransactionExceptions.DestinationEwalletUserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDestinationEwalletUserNotFoundException(TransactionExceptions.DestinationEwalletUserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.NOT_FOUND
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "An unexpected error occurred",
                "status", HttpStatus.INTERNAL_SERVER_ERROR
        ));
    }
}
