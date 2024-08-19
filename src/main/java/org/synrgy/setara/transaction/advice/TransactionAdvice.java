package org.synrgy.setara.transaction.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.transaction.exception.JasperReportExceptions;
import org.synrgy.setara.transaction.exception.TransactionExceptions;

@ControllerAdvice(basePackages = "org.synrgy.setara.transaction")
public class TransactionAdvice {
    private static final Logger log = LoggerFactory.getLogger(TransactionAdvice.class);

    @ExceptionHandler(TransactionExceptions.UserNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleUserNotFoundException(TransactionExceptions.UserNotFoundException ex) {
        log.error("User not found: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.NOT_FOUND, "User not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.DestinationEwalletUserNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleDestinationEwalletUserNotFoundException(TransactionExceptions.DestinationEwalletUserNotFoundException ex) {
        log.error("Destination e-wallet user not found: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.NOT_FOUND, "Destination e-wallet user not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.InvalidMpinException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidMpinException(TransactionExceptions.InvalidMpinException ex) {
        log.error("Invalid MPIN: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid MPIN");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTopUpAmountException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidTopUpAmountException(TransactionExceptions.InvalidTopUpAmountException ex) {
        log.error("Invalid top-up amount: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid top-up amount");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InsufficientBalanceException.class)
    public ResponseEntity<BaseResponse<String>> handleInsufficientBalanceException(TransactionExceptions.InsufficientBalanceException ex) {
        log.error("Insufficient balance: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Insufficient balance");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.DestinationAccountNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleDestinationAccountNotFoundException(TransactionExceptions.DestinationAccountNotFoundException ex) {
        log.error("Destination account not found: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.NOT_FOUND, "Destination account not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.InvalidMonthException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidMonthException(TransactionExceptions.InvalidMonthException ex) {
        log.error("Invalid month: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid month");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InvalidYearException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidYearException(TransactionExceptions.InvalidYearException ex) {
        log.error("Invalid year: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid year");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.TransactionNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleTransactionNotFoundException(TransactionExceptions.TransactionNotFoundException ex) {
        log.error("Transaction not found: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.NOT_FOUND, "Transaction not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.TransactionNotOwnedByUser.class)
    public ResponseEntity<BaseResponse<String>> handleTransactionNotOwnedByUser(TransactionExceptions.TransactionNotOwnedByUser ex) {
        log.error("Transaction not owned by user: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.UNAUTHORIZED, "Transaction not owned by user");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTransactionAmountException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidTransactionAmountException(TransactionExceptions.InvalidTransactionAmountException ex) {
        log.error("Invalid transaction amount: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid transaction amount");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.MerchantNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleMerchantNotFoundException(TransactionExceptions.MerchantNotFoundException ex) {
        log.error("Merchant not found: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.NOT_FOUND, "Merchant not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTransferAmountException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidTransferAmountException(TransactionExceptions.InvalidTransferAmountException ex) {
        log.error("Invalid transfer amount: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid transfer amount");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionExceptions.InvalidTransferDestinationException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidTransferDestinationException(TransactionExceptions.InvalidTransferDestinationException ex) {
        log.error("Invalid transfer destination: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid transfer destination");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Invalid JSON format: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JasperReportExceptions.ReportFailedToLoadException.class)
    public ResponseEntity<BaseResponse<String>> handleReportFailedToLoadException(JasperReportExceptions.ReportFailedToLoadException ex) {
        log.error("Failed to load JasperReport template: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to load JasperReport template");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JasperReportExceptions.ReportFillOrExportException.class)
    public ResponseEntity<BaseResponse<String>> handleReportFillOrExportException(JasperReportExceptions.ReportFillOrExportException ex) {
        log.error("Failed to fill or export JasperReport: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fill or export JasperReport");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JasperReportExceptions.SavePdfException.class)
    public ResponseEntity<BaseResponse<String>> handleSavePdfException(JasperReportExceptions.SavePdfException ex) {
        log.error("Failed to save PDF file: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save PDF file");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleGenericException(Exception ex) {
        log.error("Unexpected Exception: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
