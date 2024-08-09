package org.synrgy.setara.transaction.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.transaction.dto.MonthlyReportResponse;
import org.synrgy.setara.transaction.service.TransactionService;
import org.synrgy.setara.user.model.User;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
}
