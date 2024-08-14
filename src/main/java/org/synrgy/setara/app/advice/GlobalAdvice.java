package org.synrgy.setara.app.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.synrgy.setara.common.dto.BaseResponse;

@RestControllerAdvice(basePackages = "org.synrgy.setara")
public class GlobalAdvice {

  private final Logger log = LoggerFactory.getLogger(GlobalAdvice.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<Void>> handleGlobalException(Exception ex) {
    log.error("Unspecified Exception: {}", ex.getMessage());
    BaseResponse<Void> body = BaseResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    log.error("Validation Exception: {}", ex.getMessage());

    String[] errors = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ":" + error.getDefaultMessage())
        .toArray(String[]::new);

    String errorMessage = String.join(";", errors);
    BaseResponse<Void> body = BaseResponse.fail(errorMessage);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

}
