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
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.UUID;

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
                                            value = "{\"idEwallet\": \"0eb30e87-3031-43d8-bd99-845ec3b7a4d9\", \"destinationPhoneNumber\": \"081234567890\", \"amount\": 10000, \"mpin\": \"170687\", \"note\": \"string\", \"savedAccount\": true}"
                                    ),
                                    @ExampleObject(
                                            name = "Andhika to Aurlyn",
                                            value = "{\"idEwallet\": \"0eb30e87-3031-43d8-bd99-845ec3b7a4d9\", \"destinationPhoneNumber\": \"081234567891\", \"amount\": 10000, \"mpin\": \"120951\", \"note\": \"string\", \"savedAccount\": true}"
                                    )
                            }
                    )
            )
    )
    @PostMapping("/topup")
    public ResponseEntity<BaseResponse<TopUpResponse>> topUp(User user, @RequestBody TopUpRequest request) {
            TopUpResponse topUpResponse = transactionService.topUp(user, request);
            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, topUpResponse, "Top-up successful"));
    }

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Kendrick to Andhika",
                                            value = "{\"destinationAccountNumber\": \"2891376451\", \"amount\": 10000, \"mpin\": \"170687\", \"note\": \"string\", \"savedAccount\": true}"
                                    ),
                                    @ExampleObject(
                                            name = "Andhika to Kendrick",
                                            value = "{\"destinationAccountNumber\": \"1122334455\", \"amount\": 10000, \"mpin\": \"120951\", \"note\": \"string\", \"savedAccount\": true}"
                                    )
                            }
                    )
            )
    )
    @PostMapping("/bca-transfer")
    public ResponseEntity<BaseResponse<TransferResponse>> bcaTransfer(User user, @RequestBody TransferRequest request) {
        TransferResponse response = transactionService.transferWithinBCA(user, request);
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, response,"Transfer successful"));
    }

    @GetMapping("/getMonthlyReport")
    public ResponseEntity<BaseResponse<MonthlyReportResponse>> getMonthlyReport(
      User user,
      @Parameter(schema = @Schema(type = "integer", example = "8")) @RequestParam(name = "month") int month,
            @Parameter(schema = @Schema(type = "integer", example = "2024")) @RequestParam(name = "year") int year) {
        MonthlyReportResponse monthlyReportResponse = transactionService.getMonthlyReport(user, month, year);
        BaseResponse<MonthlyReportResponse> response = BaseResponse.success(HttpStatus.OK, monthlyReportResponse, "Success Get Monthly Report");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/merchant-transaction")
    public ResponseEntity<BaseResponse<MerchantTransactionResponse>> merchantTransaction(User user, @RequestBody MerchantTransactionRequest request) {
        MerchantTransactionResponse response = transactionService.merchantTransaction(user, request);
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, response, "Transaction successful"));
    }

    @PostMapping("/get-all-mutation")
    public ResponseEntity<BaseResponse<List<MutationResponse>>> getAllMutation(User user,
                                                                               @RequestBody MutationRequest request,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        Page<MutationResponse> mutationResponsePage = transactionService.getAllMutation(user, request, page, size);
        List<MutationResponse> mutationResponses = mutationResponsePage.getContent();
        BaseResponse<List<MutationResponse>> response = BaseResponse.success(HttpStatus.OK, mutationResponses, "Success Get All Mutation");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-mutation-detail/{transactionId}")
    public ResponseEntity<BaseResponse<MutationDetailResponse>> getMutationDetail(User user, @PathVariable UUID transactionId) {
        MutationDetailResponse response = transactionService.getMutationDetail(user, transactionId);
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, response, "Success Get Mutation Detail"));
    }
}
