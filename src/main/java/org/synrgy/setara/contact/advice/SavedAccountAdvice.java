package org.synrgy.setara.contact.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.contact.exception.SavedAccountExceptions.*;

@ControllerAdvice(basePackages = "org.synrgy.setara.contact")
public class SavedAccountAdvice {

    private static final Logger log = LoggerFactory.getLogger(SavedAccountAdvice.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("UserNotFoundException: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(SavedAccountNotFoundException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> handleSavedAccountNotFoundException(SavedAccountNotFoundException ex) {
        log.error("SavedAccountNotFoundException: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(FavoriteUpdateException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> handleFavoriteUpdateException(FavoriteUpdateException ex) {
        log.error("FavoriteUpdateException: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Invalid JSON format: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> handleGenericException(Exception ex) {
        log.error("Unexpected Exception: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
