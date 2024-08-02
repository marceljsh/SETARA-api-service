package org.synrgy.setara.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.exception.UserExceptions;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.repository.BankRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BankRepository bankRepository;

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
    void testGetBalance_UserExists() {
        User user = new User();
        user.setBalance(BigDecimal.valueOf(100000));
        user.setSignature("test_signature");
        when(userRepository.findBySignature("test_signature")).thenReturn(Optional.of(user));

        UserBalanceResponse response = userService.getBalance();

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(100000), response.getBalance());
    }

    @Test
    void testGetBalance_UserNotFound() {
        when(userRepository.findBySignature("test_signature")).thenReturn(Optional.empty());

        assertThrows(UserExceptions.UserNotFoundException.class, () -> userService.getBalance());
    }
}
