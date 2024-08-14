package org.synrgy.setara.auth.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.synrgy.setara.common.dto.BaseResponse;

import javax.naming.AuthenticationException;

@RestControllerAdvice(basePackages = "org.synrgy.setara.auth")
public class AuthenticationAdvice {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAdvice.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        logger.error("Authentication error: {}", ex.getMessage());
        BaseResponse<Void> body = BaseResponse.fail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        logger.error("Bad credentials: {}", ex.getMessage());
        BaseResponse<Void> body = BaseResponse.fail("Bad credentials: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
