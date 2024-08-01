package org.synrgy.setara.transaction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.transaction.dto.*;
import org.synrgy.setara.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

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
    public ResponseEntity<BaseResponse<MonthlyReportResponse>> getMonthlyReport(
            @Parameter(
                    name = "month",
                    required = true,
                    schema = @Schema(type = "integer", example = "7")
            ) @RequestParam(name = "month") int month,
            @Parameter(
                    name = "year",
                    required = true,
                    schema = @Schema(type = "integer", example = "2024")
            ) @RequestParam(name = "year") int year) {

        MonthlyReportResponse monthlyReportResponse = transactionService.getMonthlyReport(month, year);
        BaseResponse<MonthlyReportResponse> response = BaseResponse.success(HttpStatus.OK, monthlyReportResponse, "Success Get Monthly Report");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/merchant-transaction")
    public ResponseEntity<BaseResponse<MerchantTransactionResponse>> merchantTransaction(@RequestBody MerchantTransactionRequest request) {
        MerchantTransactionResponse response = transactionService.merchantTransaction(request);
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, response, "Transaction successful"));
    }

    @PostMapping("/get-all-mutation")
    public ResponseEntity<BaseResponse<List<MutationResponse>>> getAllMutation(@RequestBody MutationRequest request,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        Page<MutationResponse> mutationResponsePage = transactionService.getAllMutation(request, page, size);
        List<MutationResponse> mutationResponses = mutationResponsePage.getContent();
        BaseResponse<List<MutationResponse>> response = BaseResponse.success(HttpStatus.OK, mutationResponses, "Success Get All Mutation");
        return ResponseEntity.ok(response);
    }
}
