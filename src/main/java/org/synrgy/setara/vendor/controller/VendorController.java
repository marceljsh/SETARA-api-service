package org.synrgy.setara.vendor.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.validation.ValidUUID;
import org.synrgy.setara.vendor.controller.media.FetchAllBanks;
import org.synrgy.setara.vendor.controller.media.FetchAllEwallets;
import org.synrgy.setara.vendor.controller.media.FetchSingleMerchant;
import org.synrgy.setara.vendor.controller.media.MerchantId;
import org.synrgy.setara.vendor.dto.BankResponse;
import org.synrgy.setara.vendor.dto.EwalletResponse;
import org.synrgy.setara.vendor.dto.MerchantResponse;
import org.synrgy.setara.vendor.service.BankService;
import org.synrgy.setara.vendor.service.EwalletService;
import org.synrgy.setara.vendor.service.MerchantService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class VendorController {

  private final Logger log = LoggerFactory.getLogger(VendorController.class);

  private final BankService bankService;
  private final EwalletService ewalletService;
  private final MerchantService merchantService;

  @FetchAllEwallets
  @GetMapping(
    value = "/ewallets",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<List<EwalletResponse>>> getAllEwallets() {
    log.info("Request to fetch all E-Wallets");

    return ResponseEntity.ok(BaseResponse.success("OK", ewalletService.fetchAll()));
  }

  @FetchAllBanks
  @GetMapping(
    value = "/banks",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<List<BankResponse>>> getAllBanks() {
    log.info("Request to fetch all banks");

    return ResponseEntity.ok(BaseResponse.success("OK", bankService.fetchAll()));
  }

  @FetchSingleMerchant
  @GetMapping(
    value = "/merchants/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<MerchantResponse>> getMerchantById(
    @MerchantId
    @ValidUUID
    @PathVariable("id")
    String id
  ) {
    log.info("Request to fetch merchant by id: {}", id);

    UUID merchantId = UUID.fromString(id);

    return ResponseEntity.ok(BaseResponse.success("OK", merchantService.fetchById(merchantId)));
  }

}
