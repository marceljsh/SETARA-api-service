package org.synrgy.setara.transaction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.transaction.dto.TransactionRequest;
import org.synrgy.setara.transaction.dto.TransactionResponse;
import org.synrgy.setara.transaction.service.TransactionService;
import org.synrgy.setara.common.utils.GenericResponse;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/topup")
    public ResponseEntity<GenericResponse<TransactionResponse>> topUp(@RequestBody TransactionRequest request, @RequestHeader("Authorization") String token) {
        try {
            String authToken = token.substring(7);
            TransactionResponse response = transactionService.topUp(request, authToken);
            return ResponseEntity.ok(GenericResponse.success(HttpStatus.OK, "Top-up successful", response));
        } catch (RuntimeException e) {
            HttpStatus status;
            String message = e.getMessage();

            if (message.contains("Invalid MPIN")) {
                status = HttpStatus.BAD_REQUEST;
            } else if (message.contains("Insufficient balance")) {
                status = HttpStatus.BAD_REQUEST;
            } else if (message.contains("Destination e-wallet user not found")) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            log.error("Error processing top-up request: {}", message);
            return ResponseEntity.status(status).body(GenericResponse.error(status, message));
        }
    }

}
