package org.synrgy.setara.transaction.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.transaction.exception.TransactionExceptions;

@ControllerAdvice(basePackages = "org.synrgy.setara.transaction")
public class TransactionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(TransactionAdvice.class);

    @ExceptionHandler(TransactionExceptions.UserNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleUserNotFoundException(TransactionExceptions.UserNotFoundException ex) {
        logger.error("User not found: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.DestinationEwalletUserNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleDestinationEwalletUserNotFoundException(TransactionExceptions.DestinationEwalletUserNotFoundException ex) {
        logger.error("Destination e-wallet user not found: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.InvalidMpinException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidMpinException(TransactionExceptions.InvalidMpinException ex) {
        logger.error("Invalid MPIN: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTopUpAmountException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidTopUpAmountException(TransactionExceptions.InvalidTopUpAmountException ex) {
        logger.error("Invalid top-up amount: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InsufficientBalanceException.class)
    public ResponseEntity<BaseResponse<String>> handleInsufficientBalanceException(TransactionExceptions.InsufficientBalanceException ex) {
        logger.error("Insufficient balance: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.DestinationAccountNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleDestinationAccountNotFoundException(TransactionExceptions.DestinationAccountNotFoundException ex) {
        logger.error("Destination account not found: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.InvalidMonthException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidMonthException(TransactionExceptions.InvalidMonthException ex) {
        logger.error("Invalid month: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InvalidYearException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidYearException(TransactionExceptions.InvalidYearException ex) {
        logger.error("Invalid year: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.TransactionNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleTransactionNotFoundException(TransactionExceptions.TransactionNotFoundException ex) {
        logger.error("Transaction not found: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.TransactionNotOwnedByUser.class)
    public ResponseEntity<BaseResponse<String>> handleTransactionNotOwnedByUser(TransactionExceptions.TransactionNotOwnedByUser ex) {
        logger.error("Transaction not owned by user: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTransactionAmountException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidTransactionAmountException(TransactionExceptions.InvalidTransactionAmountException ex) {
        logger.error("Invalid transaction amount: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.MerchantNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleMerchantNotFoundException(TransactionExceptions.MerchantNotFoundException ex) {
        logger.error("Merchant not found: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTransferAmountException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidTransferAmountException(TransactionExceptions.InvalidTransferAmountException ex) {
        logger.error("Invalid transfer amount: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTransferDestinationException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidTransferDestinationException(TransactionExceptions.InvalidTransferDestinationException ex) {
        logger.error("Invalid transfer destination: {}", ex.getMessage());
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("Invalid JSON format: {}", ex.getMessage());
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> handleGenericException(Exception ex) {
        logger.error("Unexpected Exception: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
