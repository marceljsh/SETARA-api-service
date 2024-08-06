package org.synrgy.setara.transaction.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.synrgy.setara.common.dto.ApiResponse;
import org.synrgy.setara.transaction.exception.ReferenceNumberGenerationException;

@ControllerAdvice(basePackages = "org.synrgy.setara.transaction")
public class TransactionAdvice {

  private static final Logger log = LoggerFactory.getLogger(TransactionAdvice.class);

  @ExceptionHandler(ReferenceNumberGenerationException.class)
  public ResponseEntity<ApiResponse<Void>> handleReferenceNumberGenerationException(ReferenceNumberGenerationException ex) {
    log.error("ReferenceNumberGenerationException: {}", ex.getMessage());
    ApiResponse<Void> body = ApiResponse.fail("Failed to generate reference number");
    return ResponseEntity.internalServerError().body(body);
  }

}
