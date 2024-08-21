package org.synrgy.setara.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBalance_Success() {
        User user = new User();

        UserBalanceResponse userBalanceResponse = UserBalanceResponse.builder()
                .checkTime(LocalDateTime.now())
                .balance(BigDecimal.valueOf(100000))
                .build();

        when(userService.getBalance(user)).thenReturn(userBalanceResponse);

        ResponseEntity<BaseResponse<UserBalanceResponse>> response = userController.getBalance(user);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success Get Balance", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(userBalanceResponse, response.getBody().getData());
    }
}
