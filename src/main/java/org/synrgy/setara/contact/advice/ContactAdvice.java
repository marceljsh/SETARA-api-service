package org.synrgy.setara.contact.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.contact.exception.BankContactNotFoundException;
import org.synrgy.setara.contact.exception.EwalletContactNotFoundException;
import org.synrgy.setara.contact.exception.FavoriteUpdateException;

@ControllerAdvice(basePackages = "org.synrgy.setara.contact")
public class ContactAdvice {

    private static final Logger log = LoggerFactory.getLogger(ContactAdvice.class);

    @ExceptionHandler(BankContactNotFoundException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<Void>> handleSavedAccountNotFoundException(BankContactNotFoundException ex) {
        log.error("BankContactNotFoundException: {}", ex.getMessage());
        BaseResponse<Void> body = BaseResponse.fail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(EwalletContactNotFoundException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<Void>> handleEwalletContactNotFoundException(EwalletContactNotFoundException ex) {
        log.error("EwalletContactNotFoundException: {}", ex.getMessage());
        BaseResponse<Void> body = BaseResponse.fail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(FavoriteUpdateException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<Void>> handleFavoriteUpdateException(FavoriteUpdateException ex) {
        log.error("FavoriteUpdateException: {}", ex.getMessage(), ex);
        BaseResponse<Void> response = BaseResponse.fail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
