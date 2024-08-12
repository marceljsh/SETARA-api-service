package org.synrgy.setara.transaction.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.synrgy.setara.transaction.exception.TransactionExceptions;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.repository.TransactionRepository;
import org.synrgy.setara.transaction.dto.MonthlyReportResponse;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.synrgy.setara.transaction.model.TransactionType.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTopUp_Success() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
    }

    @Test
    void testTopUp_InvalidMpin() {

    }

    @Test
    void testTopUp_InvalidTopUpAmount() {

    }

    @Test
    void testTopUp_InsufficientBalance() {

    }

    @Test
    void testTopUp_DestinationEwalletUserNotFound() {

    }

    @Test
    void testGetMonthlyReport_Success() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setPhoneNumber("123456789");
        user.setAccountNumber("987654321");
        when(userRepository.findBySignature("test_signature")).thenReturn(Optional.of(user));

        Transaction deposit = new Transaction();
        deposit.setType(DEPOSIT);
        deposit.setTotalamount(BigDecimal.valueOf(200000));

        Transaction transfer = new Transaction();
        transfer.setType(TRANSFER);
        transfer.setTotalamount(BigDecimal.valueOf(100000));

        Transaction topUp = new Transaction();
        topUp.setType(TOP_UP);
        topUp.setTotalamount(BigDecimal.valueOf(50000));

        List<Transaction> transactions = List.of(deposit, transfer, topUp);
        when(transactionRepository.findByUserAndMonthAndYear(user.getId(), 7, 2024)).thenReturn(transactions);
        when(transactionRepository.findTransfersByPhoneNumberAndMonthAndYear(user.getPhoneNumber(), 7, 2024)).thenReturn(Collections.emptyList());
        when(transactionRepository.findTransfersByAccountNumberAndMonthAndYear(user.getAccountNumber(), 7, 2024)).thenReturn(Collections.emptyList());

        MonthlyReportResponse response = transactionService.getMonthlyReport(user, 7, 2024);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(200000), response.getIncome());
        assertEquals(BigDecimal.valueOf(150000), response.getExpense());
        assertEquals(BigDecimal.valueOf(50000), response.getTotal());
    }

    @Test
    void testGetMonthlyReport_NoTransaction() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setPhoneNumber("123456789");
        user.setAccountNumber("987654321");
        when(userRepository.findBySignature("test_signature")).thenReturn(Optional.of(user));

        when(transactionRepository.findByUserAndMonthAndYear(user.getId(), 7, 2024)).thenReturn(Collections.emptyList());
        when(transactionRepository.findTransfersByPhoneNumberAndMonthAndYear(user.getPhoneNumber(), 7, 2024)).thenReturn(Collections.emptyList());
        when(transactionRepository.findTransfersByAccountNumberAndMonthAndYear(user.getAccountNumber(), 7, 2024)).thenReturn(Collections.emptyList());

        MonthlyReportResponse response = transactionService.getMonthlyReport(user, 7, 2024);

        assertEquals(BigDecimal.valueOf(0), response.getIncome());
        assertEquals(BigDecimal.valueOf(0), response.getExpense());
        assertEquals(BigDecimal.valueOf(0), response.getTotal());
    }

    @Test
    void testGetMonthlyReport_InvalidMonth1() {
        assertThrows(TransactionExceptions.InvalidMonthException.class, () -> transactionService.getMonthlyReport(new User(), 13, 2024));
    }

    @Test
    void testGetMonthlyReport_InvalidMonth2() {
        assertThrows(TransactionExceptions.InvalidMonthException.class, () -> transactionService.getMonthlyReport(new User(), 0, 2024));
    }

    @Test
    void testGetMonthlyReport_InvalidYear1() {
        assertThrows(TransactionExceptions.InvalidYearException.class, () -> transactionService.getMonthlyReport(new User(), 7, 1899));
    }

    @Test
    void testGetMonthlyReport_InvalidYear2() {
        assertThrows(TransactionExceptions.InvalidYearException.class, () -> transactionService.getMonthlyReport(new User(), 7, 2100));
    }

    @Test
    void testMerchantTransaction_Success() {

    }

    @Test
    void testMerchantTransaction_InvalidMpin() {

    }

    @Test
    void testMerchantTransaction_InvalidTopUpAmount() {

    }

    @Test
    void testMerchantTransaction_InsufficientBalance() {

    }

    @Test
    void testMerchantTransaction_MerchantNotFound() {

    }
}
