package org.synrgy.setara.user.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.synrgy.setara.common.dto.ApiResponse;
import org.synrgy.setara.user.exception.EwalletUserNotFoundException;
import org.synrgy.setara.user.exception.UserNotFoundException;

@ControllerAdvice(basePackages = "org.synrgy.setara.user")
public class UserAdvice {

  private static final Logger log = LoggerFactory.getLogger(UserAdvice.class);

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException ex) {
    log.error("UserNotFoundException: {}", ex.getMessage());
    ApiResponse<Void> body = ApiResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(EwalletUserNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleEwalletUserNotFoundException(EwalletUserNotFoundException ex) {
    log.error("EwalletUserNotFoundException: {}", ex.getMessage());
    ApiResponse<Void> body = ApiResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

}
