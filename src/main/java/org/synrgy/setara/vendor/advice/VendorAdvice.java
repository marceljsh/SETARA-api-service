package org.synrgy.setara.vendor.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.synrgy.setara.common.dto.ApiResponse;
import org.synrgy.setara.vendor.exception.*;

@RestControllerAdvice(basePackages = "org.synrgy.setara.vendor")
public class VendorAdvice {

  private final Logger log = LoggerFactory.getLogger(VendorAdvice.class);

  @ExceptionHandler(BankNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleBankNotFoundException(BankNotFoundException ex) {
    log.error("BankNotFoundException: {}", ex.getMessage());
    ApiResponse<Void> body = ApiResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(EwalletNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleEwalletNotFoundException(EwalletNotFoundException ex) {
    log.error("EwalletNotFoundException: {}", ex.getMessage());
    ApiResponse<Void> body = ApiResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(NmidGenerationException.class)
  public ResponseEntity<ApiResponse<Void>> handleNmidGenerationException(NmidGenerationException ex) {
    log.error("NmidGenerationException: {}", ex.getMessage());
    ApiResponse<Void> body = ApiResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(TerminalIdGenerationException.class)
  public ResponseEntity<ApiResponse<Void>> handleTerminalIdGenerationException(TerminalIdGenerationException ex) {
    log.error("TerminalIdGenerationException: {}", ex.getMessage());
    ApiResponse<Void> body = ApiResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(QRCodeGenerationException.class)
  public ResponseEntity<ApiResponse<Void>> handleQRCodeGenerationException(QRCodeGenerationException ex) {
    log.error("QRCodeGenerationException: {}", ex.getMessage());
    ApiResponse<Void> body = ApiResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(MerchantNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleMerchantNotFoundException(MerchantNotFoundException ex) {
    log.error("MerchantNotFoundException: {}", ex.getMessage());
    ApiResponse<Void> body = ApiResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

}
