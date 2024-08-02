package org.synrgy.setara.transaction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
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

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Kendrick to Farah",
                                            value = "{\"destinationPhoneNumber\": \"081234567890\", \"amount\": 0, \"mpin\": \"170687\", \"note\": \"string\", \"savedAccount\": true}"
                                    ),
                                    @ExampleObject(
                                            name = "Andhika to Aurlyn",
                                            value = "{\"destinationPhoneNumber\": \"081234567891\", \"amount\": 0, \"mpin\": \"120951\", \"note\": \"string\", \"savedAccount\": true}"
                                    )
                            }
                    )
            )
    )
    @PostMapping("/topup")
    public ResponseEntity<BaseResponse<TopUpResponse>> topUp(@RequestBody TopUpRequest request) {
            TopUpResponse topUpResponse = transactionService.topUp(request);
            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, topUpResponse, "Top-up successful"));
    }

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Kendrick to Andhika",
                                            value = "{\"destinationAccountNumber\": \"2891376451\", \"amount\": 0, \"mpin\": \"170687\", \"note\": \"string\", \"savedAccount\": true}"
                                    ),
                                    @ExampleObject(
                                            name = "Andhika to Kendrick",
                                            value = "{\"destinationAccountNumber\": \"1122334455\", \"amount\": 0, \"mpin\": \"120951\", \"note\": \"string\", \"savedAccount\": true}"
                                    )
                            }
                    )
            )
    )
    @PostMapping("/bca-transfer")
    public ResponseEntity<BaseResponse<TransferResponse>> bcaTransfer(@RequestBody TransferRequest request) {
        TransferResponse response = transactionService.transferWithinBCA(request);
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, response,"Transfer successful"));
    }

    @GetMapping("/getMonthlyReport")
    public ResponseEntity<BaseResponse<MonthlyReportResponse>> getMonthlyReport(
            @Parameter(schema = @Schema(type = "integer", example = "8")) @RequestParam(name = "month") int month,
            @Parameter(schema = @Schema(type = "integer", example = "2024")) @RequestParam(name = "year") int year) {
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
