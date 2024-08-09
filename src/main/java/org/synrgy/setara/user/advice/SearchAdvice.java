package org.synrgy.setara.user.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.exception.SearchExceptions;

@ControllerAdvice(basePackages = "org.synrgy.setara.user")
public class SearchAdvice {
    private static final Logger log = LoggerFactory.getLogger(SearchAdvice.class);

    @ExceptionHandler(SearchExceptions.SearchNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleSearchNotFoundException() {
        log.error("Search not found");
        return new ResponseEntity<>(BaseResponse.failure(HttpStatus.NOT_FOUND, "Search not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<String>> handleHttpMessageNotReadableException() {
        log.error("Invalid JSON format");
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> handleGenericException() {
        log.error("Unexpected Exception");
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
