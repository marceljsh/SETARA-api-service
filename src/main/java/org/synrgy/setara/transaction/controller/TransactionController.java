package org.synrgy.setara.transaction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.transaction.dto.*;
import org.synrgy.setara.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/topup")
    public ResponseEntity<BaseResponse<TopUpResponse>> topUp(@RequestBody TopUpRequest request) {
            TopUpResponse topUpResponse = transactionService.topUp(request);
            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, topUpResponse, "Top-up successful"));
    }

    @PostMapping("/bca-transfer")
    public ResponseEntity<BaseResponse<TransferResponse>> bcaTransfer(@RequestBody TransferRequest request) {
        TransferResponse response = transactionService.transferWithinBCA(request);
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, response,"Transfer successful"));
    }

    @GetMapping("/getMonthlyReport")
    public ResponseEntity<BaseResponse<MonthlyReportResponse>> getMonthlyReport(@RequestParam(name = "month") int month, @RequestParam(name = "year") int year) {
        MonthlyReportResponse monthlyReportResponse = transactionService.getMonthlyReport(month, year);
        BaseResponse<MonthlyReportResponse> response = BaseResponse.success(HttpStatus.OK, monthlyReportResponse, "Success Get Monthly Report");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/merchant-transaction")
    public ResponseEntity<BaseResponse<MerchantTransactionResponse>> merchantTransaction(@RequestBody MerchantTransactionRequest request) {
        MerchantTransactionResponse response = transactionService.merchantTransaction(request);
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, response, "Transaction successful"));
    }
}
