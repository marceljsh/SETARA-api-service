package org.synrgy.setara.transaction.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.transaction.dto.*;
import org.synrgy.setara.transaction.service.TransactionService;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.vendor.model.Bank;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTopUp_Success() {
        User user = new User();

        TopUpRequest request = TopUpRequest.builder().build();

        TopUpResponse expectedResponse = TopUpResponse.builder()
                .user(new TopUpResponse.UserDto())
                .userEwallet(new TopUpResponse.UserEwalletDto())
                .amount(BigDecimal.valueOf(20000))
                .adminFee(BigDecimal.valueOf(1000))
                .totalAmount(BigDecimal.valueOf(21000))
                .build();

        when(transactionService.topUp(user, request)).thenReturn(expectedResponse);

        ResponseEntity<BaseResponse<TopUpResponse>> response = transactionController.topUp(user, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Top-up successful", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(expectedResponse, response.getBody().getData());
    }

    @Test
    void testGetMonthlyReport_Success() {
        User user = new User();

        MonthlyReportResponse monthlyReportResponse = MonthlyReportResponse.builder()
                .income(BigDecimal.valueOf(50000))
                .expense(BigDecimal.valueOf(20000))
                .total(BigDecimal.valueOf(30000))
                .build();

        when(transactionService.getMonthlyReport(user, 8, 2024)).thenReturn(monthlyReportResponse);

        ResponseEntity<BaseResponse<MonthlyReportResponse>> response = transactionController.getMonthlyReport(user, 8, 2024);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success Get Monthly Report", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(monthlyReportResponse, response.getBody().getData());
    }

    @Test
    void testMerchantTransaction_Success() {
        User user = new User();

        MerchantTransactionRequest request = MerchantTransactionRequest.builder().build();

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

        when(transactionService.merchantTransaction(user, request)).thenReturn(expectedResponse);

        ResponseEntity<BaseResponse<MerchantTransactionResponse>> response = transactionController.merchantTransaction(user, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction successful", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(expectedResponse, response.getBody().getData());
    }

    @Test
    void testGetMutationDetail_Success() {
        User user = User.builder()
                .name("John Doe")
                .bank(Bank.builder().name("Tahapan BCA").build())
                .accountNumber("123456789")
                .imagePath("/images/johndoe.png")
                .build();

        UUID mutationDetailId = UUID.randomUUID();

        MutationDetailResponse expectedResponse = MutationDetailResponse.builder()
                .sender(MutationDetailResponse.MutationUser.builder().build())
                .receiver(MutationDetailResponse.MutationUser.builder().build())
                .amount(BigDecimal.valueOf(100000))
                .adminFee(BigDecimal.ZERO)
                .totalAmount(BigDecimal.valueOf(100000))
                .build();

        when(transactionService.getMutationDetail(user, mutationDetailId)).thenReturn(expectedResponse);

        ResponseEntity<BaseResponse<MutationDetailResponse>> response = transactionController.getMutationDetail(user, mutationDetailId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success Get Mutation Detail", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(expectedResponse, response.getBody().getData());
    }
}
