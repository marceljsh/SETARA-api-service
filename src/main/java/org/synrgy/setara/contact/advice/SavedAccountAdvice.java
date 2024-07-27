package org.synrgy.setara.contact.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.contact.exception.SavedAccountExceptions.*;

@ControllerAdvice(basePackages = "org.synrgy.setara.contact")
public class SavedAccountAdvice {

    private static final Logger log = LoggerFactory.getLogger(SavedAccountAdvice.class);

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
}
