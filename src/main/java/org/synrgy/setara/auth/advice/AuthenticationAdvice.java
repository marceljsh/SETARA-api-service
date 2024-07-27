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
    public ResponseEntity<BaseResponse<?>> handleAuthenticationException(AuthenticationException ex) {
        logger.error("Authentication error: {}", ex.getMessage());
        BaseResponse<?> response = BaseResponse.failure(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<?>> handleBadCredentialsException(BadCredentialsException ex) {
        logger.error("Bad credentials: {}", ex.getMessage());
        BaseResponse<?> response = BaseResponse.failure(HttpStatus.UNAUTHORIZED, "Bad credentials");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
