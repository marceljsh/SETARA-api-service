package org.synrgy.setara.transaction.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.synrgy.setara.contact.repository.SavedAccountRepository;
import org.synrgy.setara.contact.repository.SavedEwalletUserRepository;
import org.synrgy.setara.transaction.dto.*;
import org.synrgy.setara.transaction.exception.TransactionExceptions;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.repository.TransactionRepository;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.model.Merchant;
import org.synrgy.setara.vendor.repository.EwalletRepository;
import org.synrgy.setara.vendor.repository.MerchantRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private SavedAccountRepository savedAccountRepository;

    @Mock
    private MerchantRepository merchantRepository;

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
    void testMerchantTransaction_Success() {
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

        Merchant merchant = Merchant.builder()
                .name("Jane Smith")
                .nmid("ID5958987675019")
                .terminalId("JYW")
                .imagePath("/images/janesmith.png")
                .address("Fernvale Rd.")
                .build();

        MerchantTransactionRequest request = MerchantTransactionRequest.builder()
                .idQris(UUID.randomUUID())
                .amount(BigDecimal.valueOf(40000))
                .note("Testing...")
                .mpin("123456")
                .build();

        when(passwordEncoder.matches(request.getMpin(), user.getMpin())).thenReturn(true);
        when(merchantRepository.findById(request.getIdQris())).thenReturn(Optional.of(merchant));

        MerchantTransactionResponse expectedResponse = MerchantTransactionResponse.builder()
                .sourceUser(MerchantTransactionResponse.SourceUserDTO.builder()
                        .name("John Doe")
                        .bank("BCA")
                        .accountNumber("987654321")
                        .imagePath("/images/johndoe.png")
                        .build())
                .destinationUser(MerchantTransactionResponse.DestinationUserDTO.builder()
                        .name("Jane Smith")
                        .nameMerchant("Jane Smith")
                        .nmid("ID5958987675019")
                        .terminalId("JYW")
                        .imagePath("/images/janesmith.png")
                        .build())
                .amount(BigDecimal.valueOf(40000))
                .adminFee(BigDecimal.ZERO)
                .totalAmount(BigDecimal.valueOf(40000))
                .note("Testing...")
                .build();

        MerchantTransactionResponse response = transactionService.merchantTransaction(user, request);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        assertEquals(BigDecimal.valueOf(60000), user.getBalance());
    }

    @Test
    void testMerchantTransaction_InvalidMpin() {
        User user = User.builder()
                .balance(BigDecimal.valueOf(30000))
                .mpin(passwordEncoder.encode("123456"))
                .build();

        MerchantTransactionRequest request = MerchantTransactionRequest.builder()
                .amount(BigDecimal.valueOf(20000))
                .mpin("654321")
                .build();

        assertThrows(TransactionExceptions.InvalidMpinException.class, () -> transactionService.merchantTransaction(user, request));
    }

    @Test
    void testMerchantTransaction_InvalidTopUpAmount() {
        User user = User.builder()
                .balance(BigDecimal.valueOf(30000))
                .mpin(passwordEncoder.encode("123456"))
                .build();

        MerchantTransactionRequest request = MerchantTransactionRequest.builder()
                .amount(BigDecimal.valueOf(0))
                .mpin("123456")
                .build();

        when(passwordEncoder.matches(request.getMpin(), user.getMpin())).thenReturn(true);

        assertThrows(TransactionExceptions.InvalidTransactionAmountException.class, () -> transactionService.merchantTransaction(user, request));
    }

    @Test
    void testMerchantTransaction_InsufficientBalance() {
        User user = User.builder()
                .balance(BigDecimal.valueOf(20000))
                .mpin(passwordEncoder.encode("123456"))
                .build();

        MerchantTransactionRequest request = MerchantTransactionRequest.builder()
                .idQris(UUID.randomUUID())
                .amount(BigDecimal.valueOf(30000))
                .mpin("123456")
                .build();

        when(passwordEncoder.matches(request.getMpin(), user.getMpin())).thenReturn(true);
        when(merchantRepository.findById(request.getIdQris())).thenReturn(Optional.of(new Merchant()));

        assertThrows(TransactionExceptions.InsufficientBalanceException.class, () -> transactionService.merchantTransaction(user, request));
    }

    @Test
    void testMerchantTransaction_MerchantNotFound() {
        User user = User.builder()
                .balance(BigDecimal.valueOf(30000))
                .mpin(passwordEncoder.encode("123456"))
                .build();

        MerchantTransactionRequest request = MerchantTransactionRequest.builder()
                .idQris(UUID.randomUUID())
                .amount(BigDecimal.valueOf(20000))
                .mpin("123456")
                .build();

        when(passwordEncoder.matches(request.getMpin(), user.getMpin())).thenReturn(true);
        when(merchantRepository.findById(request.getIdQris())).thenReturn(Optional.empty());

        assertThrows(TransactionExceptions.MerchantNotFoundException.class, () -> transactionService.merchantTransaction(user, request));
    }

    @Test
    void testGetMutationDetail_SuccessTopUp() {
        User user = User.builder()
                .name("John Doe")
                .bank(Bank.builder().name("Tahapan BCA").build())
                .accountNumber("123456789")
                .imagePath("/images/johndoe.png")
                .build();

        Ewallet ewallet = Ewallet.builder()
                .name("ovo")
                .build();
        ewallet.setId(UUID.randomUUID());

        EwalletUser ewalletUser = EwalletUser.builder()
                .name("Jane Smith")
                .ewallet(ewallet)
                .phoneNumber("987654321")
                .imagePath("/images/janesmith.png")
                .build();

        UUID mutationDetailId = UUID.randomUUID();
        Transaction transaction = Transaction.builder()
                .user(user)
                .ewallet(ewalletUser.getEwallet())
                .type(TOP_UP)
                .destinationPhoneNumber("987654321")
                .amount(BigDecimal.valueOf(100000))
                .adminFee(BigDecimal.valueOf(1000))
                .totalamount(BigDecimal.valueOf(101000))
                .note("Testing...")
                .time(LocalDateTime.now())
                .build();
        transaction.setId(mutationDetailId);

        when(transactionRepository.findById(mutationDetailId)).thenReturn(Optional.of(transaction));
        when(ewalletUserRepository.findByPhoneNumberAndEwallet(transaction.getDestinationPhoneNumber(), transaction.getEwallet())).thenReturn(Optional.of(ewalletUser));

        MutationDetailResponse.MutationUser sender = MutationDetailResponse.MutationUser.builder()
                .name(user.getName())
                .accountNumber(user.getAccountNumber())
                .imagePath(user.getImagePath())
                .vendorName("Tahapan BCA")
                .build();

        MutationDetailResponse.MutationUser receiver = MutationDetailResponse.MutationUser.builder()
                .name(ewalletUser.getName())
                .accountNumber(ewalletUser.getPhoneNumber())
                .imagePath(ewalletUser.getImagePath())
                .vendorName(transaction.getEwallet().getName())
                .build();

        MutationDetailResponse expectedResponse = MutationDetailResponse.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(BigDecimal.valueOf(100000))
                .adminFee(BigDecimal.valueOf(1000))
                .totalAmount(BigDecimal.valueOf(101000))
                .note("Testing...")
                .build();

        MutationDetailResponse response = transactionService.getMutationDetail(user, mutationDetailId);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetMutationDetail_SuccessTransaction() {
        User user = User.builder()
                .name("John Doe")
                .bank(Bank.builder().name("Tahapan BCA").build())
                .accountNumber("123456789")
                .imagePath("/images/johndoe.png")
                .build();

        User destinationUser = User.builder()
                .name("Jane Smith")
                .bank(Bank.builder().name("Tahapan BCA").build())
                .accountNumber("987654321")
                .imagePath("/images/janesmith.png")
                .build();

        UUID mutationDetailId = UUID.randomUUID();
        Transaction transaction = Transaction.builder()
                .user(user)
                .bank(user.getBank())
                .type(TRANSFER)
                .destinationAccountNumber("987654321")
                .amount(BigDecimal.valueOf(100000))
                .adminFee(BigDecimal.ZERO)
                .totalamount(BigDecimal.valueOf(100000))
                .note("Testing...")
                .time(LocalDateTime.now())
                .build();
        transaction.setId(mutationDetailId);

        when(transactionRepository.findById(mutationDetailId)).thenReturn(Optional.of(transaction));
        when(userRepository.findByAccountNumber(transaction.getDestinationAccountNumber())).thenReturn(Optional.of(destinationUser));

        MutationDetailResponse.MutationUser sender = MutationDetailResponse.MutationUser.builder()
                .name(user.getName())
                .accountNumber(user.getAccountNumber())
                .imagePath(user.getImagePath())
                .vendorName("Tahapan BCA")
                .build();

        MutationDetailResponse.MutationUser receiver = MutationDetailResponse.MutationUser.builder()
                .name(destinationUser.getName())
                .accountNumber(destinationUser.getAccountNumber())
                .imagePath(destinationUser.getImagePath())
                .vendorName(transaction.getBank().getName())
                .build();

        MutationDetailResponse expectedResponse = MutationDetailResponse.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(BigDecimal.valueOf(100000))
                .adminFee(BigDecimal.ZERO)
                .totalAmount(BigDecimal.valueOf(100000))
                .note("Testing...")
                .build();

        MutationDetailResponse response = transactionService.getMutationDetail(user, mutationDetailId);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetMutationDetail_SuccessDeposit() {
        User user = User.builder()
                .name("John Doe")
                .bank(Bank.builder().name("Tahapan BCA").build())
                .accountNumber("123456789")
                .imagePath("/images/johndoe.png")
                .build();

        User sourceUser = User.builder()
                .name("Jane Smith")
                .bank(Bank.builder().name("Tahapan BCA").build())
                .accountNumber("987654321")
                .imagePath("/images/janesmith.png")
                .build();

        UUID mutationDetailId = UUID.randomUUID();
        Transaction transaction = Transaction.builder()
                .user(user)
                .bank(user.getBank())
                .type(DEPOSIT)
                .destinationAccountNumber("123456789")
                .amount(BigDecimal.valueOf(100000))
                .adminFee(BigDecimal.ZERO)
                .totalamount(BigDecimal.valueOf(100000))
                .referenceNumber("DPT-12345")
                .note("Testing...")
                .time(LocalDateTime.now())
                .build();
        transaction.setId(mutationDetailId);

        Transaction transfer = Transaction.builder()
                .user(sourceUser)
                .bank(sourceUser.getBank())
                .type(TRANSFER)
                .destinationAccountNumber("123456789")
                .amount(BigDecimal.valueOf(100000))
                .adminFee(BigDecimal.ZERO)
                .totalamount(BigDecimal.valueOf(100000))
                .referenceNumber("TRF-12345")
                .note("Testing...")
                .time(LocalDateTime.now())
                .build();

        when(transactionRepository.findById(mutationDetailId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.findByReferenceNumber(transaction.getReferenceNumber().replace("DPT-", "TRF-"))).thenReturn(Optional.of(transfer));

        MutationDetailResponse.MutationUser sender = MutationDetailResponse.MutationUser.builder()
                .name(sourceUser.getName())
                .accountNumber(sourceUser.getAccountNumber())
                .imagePath(sourceUser.getImagePath())
                .vendorName(transaction.getBank().getName())
                .build();

        MutationDetailResponse.MutationUser receiver = MutationDetailResponse.MutationUser.builder()
                .name(user.getName())
                .accountNumber(user.getAccountNumber())
                .imagePath(user.getImagePath())
                .vendorName("Tahapan BCA")
                .build();

        MutationDetailResponse expectedResponse = MutationDetailResponse.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(BigDecimal.valueOf(100000))
                .adminFee(BigDecimal.ZERO)
                .totalAmount(BigDecimal.valueOf(100000))
                .note("Testing...")
                .build();

        MutationDetailResponse response = transactionService.getMutationDetail(user, mutationDetailId);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetMutationDetail_NotFound() {
        UUID mutationDetailId = UUID.randomUUID();

        when(transactionRepository.findById(mutationDetailId)).thenReturn(Optional.empty());

        assertThrows(TransactionExceptions.TransactionNotFoundException.class, () -> transactionService.getMutationDetail(new User(), mutationDetailId));
    }

    @Test
    void testGetMutationDetail_TransactionNotOwnedByUser() {
        User user = User.builder()
                .name("John Doe")
                .bank(Bank.builder().name("Tahapan BCA").build())
                .accountNumber("123456789")
                .imagePath("/images/johndoe.png")
                .build();

        User user2 = User.builder()
                .name("Jane Smith")
                .bank(Bank.builder().name("Tahapan BCA").build())
                .accountNumber("987654321")
                .imagePath("/images/janesmith.png")
                .build();

        UUID mutationDetailId = UUID.randomUUID();
        Transaction transaction = Transaction.builder()
                .user(user2)
                .bank(user2.getBank())
                .type(TRANSFER)
                .destinationAccountNumber("987654321")
                .amount(BigDecimal.valueOf(100000))
                .adminFee(BigDecimal.ZERO)
                .totalamount(BigDecimal.valueOf(100000))
                .note("Testing...")
                .time(LocalDateTime.now())
                .build();
        transaction.setId(mutationDetailId);

        when(transactionRepository.findById(mutationDetailId)).thenReturn(Optional.of(transaction));

        assertThrows(TransactionExceptions.TransactionNotOwnedByUser.class, () -> transactionService.getMutationDetail(user, mutationDetailId));
    }
}
