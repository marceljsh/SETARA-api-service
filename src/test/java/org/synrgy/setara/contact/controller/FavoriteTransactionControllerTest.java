package org.synrgy.setara.contact.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.contact.dto.FavoriteTransactionsResponse;
import org.synrgy.setara.contact.service.FavoriteTransactionService;
import org.synrgy.setara.transaction.model.TransactionType;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class FavoriteTransactionControllerTest {
    @Mock
    private FavoriteTransactionService favoriteTransactionService;

    @InjectMocks
    private FavoriteTransactionController favoriteTransactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFavoriteTransactions_Success() {
        User user = new User();

        List<FavoriteTransactionsResponse> expectedResponse = List.of(
                FavoriteTransactionsResponse.builder()
                        .type(TransactionType.TOP_UP)
                        .bankOrEwalletName("OVO")
                        .name("Tulip")
                        .build(),
                FavoriteTransactionsResponse.builder()
                        .type(TransactionType.TRANSFER)
                        .bankOrEwalletName("BCA")
                        .name("John Doe")
                        .build(),
                FavoriteTransactionsResponse.builder()
                        .type(TransactionType.TRANSFER)
                        .bankOrEwalletName("BCA")
                        .name("Jane Smith")
                        .build(),
                FavoriteTransactionsResponse.builder()
                        .type(TransactionType.TRANSFER)
                        .bankOrEwalletName("BCA")
                        .name("Mary Sue")
                        .build(),
                FavoriteTransactionsResponse.builder()
                        .type(TransactionType.TOP_UP)
                        .bankOrEwalletName("OVO")
                        .name("Rose")
                        .build()
        );

        when(favoriteTransactionService.getFavoriteTransactions(user)).thenReturn(expectedResponse);

        ResponseEntity<BaseResponse<List<FavoriteTransactionsResponse>>> response = favoriteTransactionController.getFavoriteTransactions(user);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success Get Favorite Transactions", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(expectedResponse, response.getBody().getData());
    }
}
