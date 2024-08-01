package org.synrgy.setara.transaction.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.transaction.exception.TransactionExceptions;

@ControllerAdvice(basePackages = "org.synrgy.setara.transaction")
public class TransactionAdvice {

    @ExceptionHandler(TransactionExceptions.UserNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleUserNotFoundException(TransactionExceptions.UserNotFoundException ex) {
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.DestinationEwalletUserNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleDestinationEwalletUserNotFoundException(TransactionExceptions.DestinationEwalletUserNotFoundException ex) {
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.InvalidMpinException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidMpinException(TransactionExceptions.InvalidMpinException ex) {
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTopUpAmountException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidTopUpAmountException(TransactionExceptions.InvalidTopUpAmountException ex) {
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InsufficientBalanceException.class)
    public ResponseEntity<BaseResponse<?>> handleInsufficientBalanceException(TransactionExceptions.InsufficientBalanceException ex) {
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.DestinationAccountNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleDestinationAccountNotFoundException(TransactionExceptions.DestinationAccountNotFoundException ex) {
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
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
    public ResponseEntity<BaseResponse<?>> handleGenericException(Exception ex) {
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
