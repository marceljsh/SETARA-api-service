package org.synrgy.setara.transaction.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.transaction.exception.TransactionExceptions;

@ControllerAdvice(basePackages = "org.synrgy.setara.transaction")
public class TransactionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(TransactionAdvice.class);

    @ExceptionHandler(TransactionExceptions.UserNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleUserNotFoundException(TransactionExceptions.UserNotFoundException ex) {
        logger.error("User not found: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.DestinationEwalletUserNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleDestinationEwalletUserNotFoundException(TransactionExceptions.DestinationEwalletUserNotFoundException ex) {
        logger.error("Destination e-wallet user not found: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.InvalidMpinException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidMpinException(TransactionExceptions.InvalidMpinException ex) {
        logger.error("Invalid MPIN: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTopUpAmountException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidTopUpAmountException(TransactionExceptions.InvalidTopUpAmountException ex) {
        logger.error("Invalid top-up amount: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InsufficientBalanceException.class)
    public ResponseEntity<BaseResponse<?>> handleInsufficientBalanceException(TransactionExceptions.InsufficientBalanceException ex) {
        logger.error("Insufficient balance: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.DestinationAccountNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleDestinationAccountNotFoundException(TransactionExceptions.DestinationAccountNotFoundException ex) {
        logger.error("Destination account not found: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.InvalidMonthException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidMonthException(TransactionExceptions.InvalidMonthException ex) {
        logger.error("Invalid month: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InvalidYearException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidYearException(TransactionExceptions.InvalidYearException ex) {
        logger.error("Invalid year: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.TransactionNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleTransactionNotFoundException(TransactionExceptions.TransactionNotFoundException ex) {
        logger.error("Transaction not found: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.TransactionNotOwnedByUser.class)
    public ResponseEntity<BaseResponse<?>> handleTransactionNotFoundException(TransactionExceptions.TransactionNotOwnedByUser ex) {
        logger.error("Transaction not owned by user");
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTransactionAmountException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidTransactionAmountException(TransactionExceptions.InvalidTransactionAmountException ex) {
        logger.error("Invalid transaction amount: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.MerchantNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleMerchantNotFoundException(TransactionExceptions.MerchantNotFoundException ex) {
        logger.error("Merchant not found: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
