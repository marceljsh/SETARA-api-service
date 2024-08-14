package org.synrgy.setara.transaction.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.common.dto.PagedResponse;
import org.synrgy.setara.transaction.dto.MonthlyReportRequest;
import org.synrgy.setara.transaction.dto.MonthlyReportResponse;
import org.synrgy.setara.transaction.dto.MutationRequest;
import org.synrgy.setara.transaction.dto.MutationResponse;
import org.synrgy.setara.transaction.dto.TopUpRequest;
import org.synrgy.setara.transaction.dto.TopUpResponse;
import org.synrgy.setara.transaction.dto.TransferRequest;
import org.synrgy.setara.transaction.dto.TransferResponse;
import org.synrgy.setara.transaction.dto.QRPaymentRequest;
import org.synrgy.setara.transaction.dto.QRPaymentResponse;
import org.synrgy.setara.transaction.service.TransactionService;
import org.synrgy.setara.user.model.User;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final Logger log = LoggerFactory.getLogger(TransactionController.class);

  private final TransactionService txService;

  @PostMapping(
    value = "/top-up",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<TopUpResponse>> topUp(User owner, @RequestBody TopUpRequest request) {
    log.info("Request to top up for {} on ewallet {}", request.getPhoneNumber(), request.getEwalletId());

    TopUpResponse data = txService.topUp(owner, request);

    return ResponseEntity.ok(BaseResponse.success("OK", data));
  }

  @PostMapping(
    value = "/transfer",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<TransferResponse>> transfer(User owner, @RequestBody TransferRequest request) {
    log.info("Request to transfer to {} by User(id={})", request.getDestAccountNumber(), owner.getId());

    TransferResponse data = txService.transfer(owner, request);

    return ResponseEntity.ok(BaseResponse.success("OK", data));
  }

  @GetMapping(
    value = "/monthly-report",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<MonthlyReportResponse>> generateMonthlyReport(User owner, @RequestBody MonthlyReportRequest request) {
    String timespan = String.format("%d/%02d", request.getYear(), request.getMonth());
    log.info("Request to generate report for User(id={}) on {}", owner.getId(), timespan);

    MonthlyReportResponse data = txService.generateMonthlyReport(owner, request.getYear(), request.getMonth());

    return ResponseEntity.ok(BaseResponse.success("OK", data));
  }

  @PostMapping(
    value = "/qr-payment",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<QRPaymentResponse>> qrPayment(User owner, @RequestBody QRPaymentRequest request) {
    log.info("Request to make QR payment to {}", request.getMerchantId());

    QRPaymentResponse data = txService.payWithQRIS(owner, request);

    return ResponseEntity.ok(BaseResponse.success("OK", data));
  }

  @GetMapping(
    value = "/mutations",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<PagedResponse<MutationResponse>>> getMutations(
      User owner,
      @RequestBody MutationRequest request,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    log.info("Request to get mutations for User(id={})", owner.getId());

    request.setPage(page);
    request.setSize(size);

    Page<MutationResponse> result = txService.getMutationList(owner, request);
    PagedResponse<MutationResponse> body = PagedResponse.from(result);

    return ResponseEntity.ok(BaseResponse.success("OK", body));
  }

  @GetMapping(
    value = "/mutations/{tx-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<MutationResponse>> getMutationDetail(@PathVariable("tx-id") UUID txId) {
    log.info("Request to get mutation detail for transaction {}", txId);

    MutationResponse data = txService.getMutationDetail(txId);

    return ResponseEntity.ok(BaseResponse.success("OK", data));
  }

}
