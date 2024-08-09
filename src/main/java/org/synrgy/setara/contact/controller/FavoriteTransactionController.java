package org.synrgy.setara.contact.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.contact.dto.FavoriteTransactionsResponse;
import org.synrgy.setara.contact.service.FavoriteTransactionService;
import org.synrgy.setara.user.model.User;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteTransactionController {

    private final FavoriteTransactionService favoriteTransactionService;

    @GetMapping("/favorite-transactions")
    public ResponseEntity<BaseResponse<List<FavoriteTransactionsResponse>>> getFavoriteTransactions(@Parameter(description = "Jangan ubah value user!") User user) {
        List<FavoriteTransactionsResponse> favoriteTransactions = favoriteTransactionService.getFavoriteTransactions(user);
        BaseResponse<List<FavoriteTransactionsResponse>> response = BaseResponse.success(HttpStatus.OK, favoriteTransactions, "Success Get Favorite Transactions");
        return ResponseEntity.ok(response);
    }
}
