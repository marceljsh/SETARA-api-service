package org.synrgy.setara.transaction.advice;

import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.transaction.exception.TransactionExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionAdvice {

    @ExceptionHandler(TransactionExceptions.InvalidMpinException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidMpinException(TransactionExceptions.InvalidMpinException ex) {
        BaseResponse<?> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InsufficientBalanceException.class)
    public ResponseEntity<BaseResponse<?>> handleInsufficientBalanceException(TransactionExceptions.InsufficientBalanceException ex) {
        BaseResponse<?> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.DestinationEwalletUserNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleDestinationEwalletUserNotFoundException(TransactionExceptions.DestinationEwalletUserNotFoundException ex) {
        BaseResponse<?> response = BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.UserNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleUserNotFoundException(TransactionExceptions.UserNotFoundException ex) {
        BaseResponse<?> response = BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.InvalidMonthException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidMonthException(TransactionExceptions.InvalidMonthException ex) {
        BaseResponse<?> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InvalidYearException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidYearException(TransactionExceptions.InvalidYearException ex) {
        BaseResponse<?> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleGeneralException(Exception ex) {
        BaseResponse<?> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
