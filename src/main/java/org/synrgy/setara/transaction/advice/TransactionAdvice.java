package org.synrgy.setara.transaction.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.transaction.exception.ReferenceNumberGenerationException;
import org.synrgy.setara.transaction.exception.TransactionNotFoundException;

@ControllerAdvice(basePackages = "org.synrgy.setara.transaction")
public class TransactionAdvice {

  private static final Logger log = LoggerFactory.getLogger(TransactionAdvice.class);

  @ExceptionHandler(ReferenceNumberGenerationException.class)
  public ResponseEntity<BaseResponse<Void>> handleReferenceNumberGenerationException(ReferenceNumberGenerationException ex) {
    log.error("ReferenceNumberGenerationException: {}", ex.getMessage());
    BaseResponse<Void> body = BaseResponse.fail("Failed to generate reference number");
    return ResponseEntity.internalServerError().body(body);
  }

  @ExceptionHandler(TransactionNotFoundException.class)
  public ResponseEntity<BaseResponse<Void>> handleTransactionNotFoundException(TransactionNotFoundException ex) {
    log.error("TransactionNotFoundException: {}", ex.getMessage());
    BaseResponse<Void> body = BaseResponse.fail("Transaction not found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

}
