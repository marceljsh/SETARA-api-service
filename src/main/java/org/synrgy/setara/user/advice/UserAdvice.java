package org.synrgy.setara.user.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.exception.UserExceptions;

@ControllerAdvice(basePackages = "org.synrgy.setara.user")
public class UserAdvice {
    @ExceptionHandler(UserExceptions.UserNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleUserNotFound(UserExceptions.UserNotFoundException ex) {
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
