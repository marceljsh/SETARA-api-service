package org.synrgy.setara.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.exception.UserExceptions;
import org.synrgy.setara.user.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test_signature");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetBalance_Success() {
        UserBalanceResponse userBalanceResponse = UserBalanceResponse.builder()
                .checkTime(LocalDateTime.now())
                .balance(BigDecimal.valueOf(100000))
                .build();

        when(userService.getBalance()).thenReturn(userBalanceResponse);

        ResponseEntity<BaseResponse<UserBalanceResponse>> response = userController.getBalance();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success Get Balance", response.getBody().getMessage());
        assertEquals(userBalanceResponse, response.getBody().getData());
    }

//    @Test
//    void testGetBalance_UserNotFound() {
//        String signature = "test_signature";
//        when(userService.getBalance()).thenThrow(new UserExceptions.UserNotFoundException("User with signature " + signature + " not found"));
//
//        ResponseEntity<BaseResponse<UserBalanceResponse>> response = userController.getBalance();
//
//        assertNotNull(response);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("User with signature " + signature + " not found", response.getBody().getMessage());
//    }
}
