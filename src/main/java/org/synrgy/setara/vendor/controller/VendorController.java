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
import org.synrgy.setara.common.dto.ApiResponse;
import org.synrgy.setara.validation.ValidUUID;
import org.synrgy.setara.vendor.service.BankService;
import org.synrgy.setara.vendor.service.EwalletService;
import org.synrgy.setara.vendor.service.MerchantService;

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

  @GetMapping(
    value = "/ewallets",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ApiResponse<Object>> getAllEwallets() {
    log.info("Request to fetch all ewallets");

    return ResponseEntity.ok(ApiResponse.success("OK", ewalletService.fetchAll()));
  }

  @GetMapping(
    value = "/banks",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ApiResponse<Object>> getAllBanks() {
    log.info("Request to fetch all banks");

    return ResponseEntity.ok(ApiResponse.success("OK", bankService.fetchAll()));
  }

  @GetMapping(
    value = "/merchants/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ApiResponse<Object>> getMerchantById(@ValidUUID @PathVariable("id") String id) {
    log.info("Request to fetch merchant by id: {}", id);

    UUID merchantId = UUID.fromString(id);

    return ResponseEntity.ok(ApiResponse.success("OK", merchantService.fetchById(merchantId)));
  }

}
