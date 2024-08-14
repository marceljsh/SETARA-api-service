package org.synrgy.setara.user.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.exception.EwalletUserNotFoundException;
import org.synrgy.setara.user.exception.UserNotFoundException;

@ControllerAdvice(basePackages = "org.synrgy.setara.user")
public class UserAdvice {

  private static final Logger log = LoggerFactory.getLogger(UserAdvice.class);

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<BaseResponse<Void>> handleUserNotFoundException(UserNotFoundException ex) {
    log.error("UserNotFoundException: {}", ex.getMessage());
    BaseResponse<Void> body = BaseResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(EwalletUserNotFoundException.class)
  public ResponseEntity<BaseResponse<Void>> handleEwalletUserNotFoundException(EwalletUserNotFoundException ex) {
    log.error("EwalletUserNotFoundException: {}", ex.getMessage());
    BaseResponse<Void> body = BaseResponse.fail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

}
