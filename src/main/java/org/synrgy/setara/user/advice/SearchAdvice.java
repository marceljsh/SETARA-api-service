package org.synrgy.setara.user.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.exception.SearchExceptions;

@ControllerAdvice(basePackages = "org.synrgy.setara.user")
public class SearchAdvice {
    private static final Logger log = LoggerFactory.getLogger(SearchAdvice.class);

    @ExceptionHandler(SearchExceptions.SearchNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleSearchNotFound(SearchExceptions.SearchNotFoundException ex) {
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
