package org.synrgy.setara.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.model.User;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBalance_Success() {
        User user = new User();
        user.setBalance(BigDecimal.valueOf(100000));
        user.setSignature("test_signature");

        UserBalanceResponse response = userService.getBalance(user);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(100000), response.getBalance());
    }
}
