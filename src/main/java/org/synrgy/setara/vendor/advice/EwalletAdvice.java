package org.synrgy.setara.vendor.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.vendor.exception.EwalletNotFoundException;

@RestControllerAdvice(basePackages = "org.synrgy.setara.vendor")
public class EwalletAdvice {
    private static final Logger logger = LoggerFactory.getLogger(EwalletAdvice.class);

    @ExceptionHandler(EwalletNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleEwalletNotFoundException(EwalletNotFoundException ex) {
        logger.error("Ewallet not found: {}", ex.getMessage());
        BaseResponse<?> response = BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
