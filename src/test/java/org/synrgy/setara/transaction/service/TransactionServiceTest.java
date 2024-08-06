//package org.synrgy.setara.transaction.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.synrgy.setara.transaction.exception.TransactionExceptions;
//import org.synrgy.setara.transaction.model.Transaction;
//import org.synrgy.setara.transaction.repository.TransactionRepository;
//import org.synrgy.setara.transaction.dto.MonthlyReportResponse;
//import org.synrgy.setara.user.model.User;
//import org.synrgy.setara.user.repository.UserRepository;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.synrgy.setara.transaction.model.TransactionType.*;
//
//public class TransactionServiceTest {
//
//    @InjectMocks
//    private TransactionServiceImpl transactionService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private TransactionRepository transactionRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        Authentication authentication = mock(Authentication.class);
//        SecurityContext securityContext = mock(SecurityContext.class);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getName()).thenReturn("test_signature");
//        SecurityContextHolder.setContext(securityContext);
//    }
//
//    @Test
//    void testGetMonthlyReport_UsersOwnTransactions() {
//        User user = new User();
//        UUID userId = UUID.randomUUID();
//        user.setId(userId);
//        user.setPhoneNumber("123456789");
//        user.setAccountNumber("987654321");
//        when(userRepository.findBySignature("test_signature")).thenReturn(Optional.of(user));
//
//        Transaction deposit = new Transaction();
//        deposit.setType(DEPOSIT);
//        deposit.setTotalamount(BigDecimal.valueOf(200000));
//
//        Transaction transfer = new Transaction();
//        transfer.setType(TRANSFER);
//        transfer.setTotalamount(BigDecimal.valueOf(100000));
//
//        Transaction topUp = new Transaction();
//        topUp.setType(TOP_UP);
//        topUp.setTotalamount(BigDecimal.valueOf(50000));
//
//        List<Transaction> transactions = List.of(deposit, transfer, topUp);
//        when(transactionRepository.findByUserAndMonthAndYear(user.getId(), 7, 2024)).thenReturn(transactions);
//        when(transactionRepository.findTransfersByPhoneNumberAndMonthAndYear(user.getPhoneNumber(), 7, 2024)).thenReturn(Collections.emptyList());
//        when(transactionRepository.findTransfersByAccountNumberAndMonthAndYear(user.getAccountNumber(), 7, 2024)).thenReturn(Collections.emptyList());
//
//        MonthlyReportResponse response = transactionService.getMonthlyReport(7, 2024);
//
//        assertNotNull(response);
//        assertEquals(BigDecimal.valueOf(200000), response.getIncome());
//        assertEquals(BigDecimal.valueOf(150000), response.getExpense());
//        assertEquals(BigDecimal.valueOf(50000), response.getTotal());
//    }
//
//    @Test
//    void testGetMonthlyReport_NoTransaction() {
//        User user = new User();
//        UUID userId = UUID.randomUUID();
//        user.setId(userId);
//        user.setPhoneNumber("123456789");
//        user.setAccountNumber("987654321");
//        when(userRepository.findBySignature("test_signature")).thenReturn(Optional.of(user));
//
//        when(transactionRepository.findByUserAndMonthAndYear(user.getId(), 7, 2024)).thenReturn(Collections.emptyList());
//        when(transactionRepository.findTransfersByPhoneNumberAndMonthAndYear(user.getPhoneNumber(), 7, 2024)).thenReturn(Collections.emptyList());
//        when(transactionRepository.findTransfersByAccountNumberAndMonthAndYear(user.getAccountNumber(), 7, 2024)).thenReturn(Collections.emptyList());
//
//        MonthlyReportResponse response = transactionService.getMonthlyReport(7, 2024);
//
//        assertEquals(BigDecimal.valueOf(0), response.getIncome());
//        assertEquals(BigDecimal.valueOf(0), response.getExpense());
//        assertEquals(BigDecimal.valueOf(0), response.getTotal());
//    }
//
//    @Test
//    void testGetMonthlyReport_UserNotFound() {
//        when(userRepository.findBySignature("test_signature")).thenReturn(Optional.empty());
//
//        assertThrows(TransactionExceptions.UserNotFoundException.class, () -> transactionService.getMonthlyReport(7, 2024));
//    }
//
//    @Test
//    void testGetMonthlyReport_InvalidMonth1() {
//        assertThrows(TransactionExceptions.InvalidMonthException.class, () -> transactionService.getMonthlyReport(13, 2024));
//    }
//
//    @Test
//    void testGetMonthlyReport_InvalidMonth2() {
//        assertThrows(TransactionExceptions.InvalidMonthException.class, () -> transactionService.getMonthlyReport(0, 2024));
//    }
//
//    @Test
//    void testGetMonthlyReport_InvalidYear1() {
//        assertThrows(TransactionExceptions.InvalidYearException.class, () -> transactionService.getMonthlyReport(7, 1899));
//    }
//
//    @Test
//    void testGetMonthlyReport_InvalidYear2() {
//        assertThrows(TransactionExceptions.InvalidYearException.class, () -> transactionService.getMonthlyReport(7, 2100));
//    }
//}
