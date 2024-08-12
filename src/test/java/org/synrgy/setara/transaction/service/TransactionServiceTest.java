package org.synrgy.setara.transaction.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.synrgy.setara.contact.repository.SavedEwalletUserRepository;
import org.synrgy.setara.transaction.dto.TopUpRequest;
import org.synrgy.setara.transaction.dto.TopUpResponse;
import org.synrgy.setara.transaction.exception.TransactionExceptions;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.repository.TransactionRepository;
import org.synrgy.setara.transaction.dto.MonthlyReportResponse;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

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
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EwalletRepository ewalletRepository;

    @Mock
    private EwalletUserRepository ewalletUserRepository;

    @Mock
    private SavedEwalletUserRepository savedEwalletUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTopUp_Success() {
        Bank bank = Bank.builder()
                .name("BCA")
                .build();
        bank.setId(UUID.randomUUID());

        User user = User.builder()
                .accountNumber("987654321")
                .name("John Doe")
                .imagePath("/images/johndoe.png")
                .balance(BigDecimal.valueOf(100000))
                .mpin(passwordEncoder.encode("123456"))
                .bank(bank)
                .build();
        user.setId(UUID.randomUUID());

        Ewallet destinationEwallet = Ewallet.builder()
                .name("ovo")
                .build();
        destinationEwallet.setId(UUID.randomUUID());

        EwalletUser destinationEwalletUser = EwalletUser.builder()
                .name("Jane Smith")
                .phoneNumber("123456789")
                .imagePath("/images/janesmith.png")
                .balance(BigDecimal.valueOf(50000))
                .ewallet(destinationEwallet)
                .build();
        destinationEwalletUser.setId(UUID.randomUUID());

        when(ewalletRepository.findById(destinationEwalletUser.getEwallet().getId())).thenReturn(Optional.of(destinationEwallet));

        TopUpRequest request = TopUpRequest.builder()
                .idEwallet(destinationEwallet.getId())
                .destinationPhoneNumber("1234567890")
                .amount(BigDecimal.valueOf(40000))
                .mpin("123456")
                .note("Testing...")
                .build();

        when(passwordEncoder.matches(request.getMpin(), user.getMpin())).thenReturn(true);
        when(ewalletUserRepository.findByEwalletIdAndPhoneNumber(request.getIdEwallet(), request.getDestinationPhoneNumber())).thenReturn(Optional.of(destinationEwalletUser));

        TopUpResponse response = transactionService.topUp(user, request);

        TopUpResponse expectedResponse = TopUpResponse.builder()
                .user(TopUpResponse.UserDto.builder()
                        .accountNumber("987654321")
                        .name("John Doe")
                        .imagePath("/images/johndoe.png")
                        .bankName("BCA")
                        .build())
                .userEwallet(TopUpResponse.UserEwalletDto.builder()
                        .name("Jane Smith")
                        .phoneNumber("123456789")
                        .imagePath("/images/janesmith.png")
                        .ewallet(TopUpResponse.EwalletDto.builder()
                                .name("ovo")
                                .build())
                        .build())
                .amount(BigDecimal.valueOf(40000))
                .adminFee(BigDecimal.valueOf(1000))
                .totalAmount(BigDecimal.valueOf(41000))
                .build();

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        assertEquals(BigDecimal.valueOf(59000), user.getBalance());
        assertEquals(BigDecimal.valueOf(90000), destinationEwalletUser.getBalance());
    }

    @Test
    void testTopUp_InvalidMpin() {
        User user = User.builder()
                .balance(BigDecimal.valueOf(30000))
                .mpin(passwordEncoder.encode("123456"))
                .build();

        TopUpRequest request = TopUpRequest.builder()
                .amount(BigDecimal.valueOf(20000))
                .mpin("654321")
                .build();

        assertThrows(TransactionExceptions.InvalidMpinException.class, () -> transactionService.topUp(user, request));
    }

    @Test
    void testTopUp_InvalidTopUpAmount() {
        User user = User.builder()
                .balance(BigDecimal.valueOf(20000))
                .mpin(passwordEncoder.encode("123456"))
                .build();

        TopUpRequest request = TopUpRequest.builder()
                .amount(BigDecimal.valueOf(0))
                .mpin("123456")
                .build();

        when(passwordEncoder.matches(request.getMpin(), user.getMpin())).thenReturn(true);

        assertThrows(TransactionExceptions.InvalidTopUpAmountException.class, () -> transactionService.topUp(user, request));
    }

    @Test
    void testTopUp_InsufficientBalance() {
        User user = User.builder()
                .balance(BigDecimal.valueOf(20000))
                .mpin(passwordEncoder.encode("123456"))
                .build();

        TopUpRequest request = TopUpRequest.builder()
                .amount(BigDecimal.valueOf(30000))
                .mpin("123456")
                .build();

        when(passwordEncoder.matches(request.getMpin(), user.getMpin())).thenReturn(true);

        assertThrows(TransactionExceptions.InsufficientBalanceException.class, () -> transactionService.topUp(user, request));
    }

    @Test
    void testTopUp_DestinationEwalletUserNotFound() {
        User user = User.builder()
                .balance(BigDecimal.valueOf(30000))
                .mpin(passwordEncoder.encode("123456"))
                .build();

        TopUpRequest request = TopUpRequest.builder()
                .amount(BigDecimal.valueOf(20000))
                .mpin("123456")
                .build();

        when(passwordEncoder.matches(request.getMpin(), user.getMpin())).thenReturn(true);
        when(ewalletUserRepository.findByEwalletIdAndPhoneNumber(request.getIdEwallet(), request.getDestinationPhoneNumber())).thenReturn(Optional.empty());

        assertThrows(TransactionExceptions.DestinationEwalletUserNotFoundException.class, () -> transactionService.topUp(user, request));
    }

    @Test
    void testGetMonthlyReport_Success() {
        User user = User.builder()
                .phoneNumber("123456789")
                .accountNumber("987654321")
                .build();
        user.setId(UUID.randomUUID());

        Transaction deposit = Transaction.builder()
                .type(DEPOSIT)
                .totalamount(BigDecimal.valueOf(200000))
                .build();

        Transaction transfer = Transaction.builder()
                .type(TRANSFER)
                .totalamount(BigDecimal.valueOf(100000))
                .build();

        Transaction topUp = Transaction.builder()
                .type(TOP_UP)
                .totalamount(BigDecimal.valueOf(50000))
                .build();

        List<Transaction> transactions = List.of(deposit, transfer, topUp);
        when(transactionRepository.findByUserAndMonthAndYear(user.getId(), 7, 2024)).thenReturn(transactions);

        MonthlyReportResponse response = transactionService.getMonthlyReport(user, 7, 2024);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(200000), response.getIncome());
        assertEquals(BigDecimal.valueOf(150000), response.getExpense());
        assertEquals(BigDecimal.valueOf(50000), response.getTotal());
    }

    @Test
    void testGetMonthlyReport_NoTransaction() {
        User user = User.builder()
                .phoneNumber("123456789")
                .accountNumber("987654321")
                .build();
        user.setId(UUID.randomUUID());

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
