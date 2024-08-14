package org.synrgy.setara.vendor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

  @GetMapping(
    value = "/ewallets",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Get all e-wallet info",
    description = "Retrieves a list of all e-wallet info",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(
            value = """
              {
                "success": true,
                "message": "OK",
                "data": [
                  {
                    "id": "e76103b2-df7d-496d-a3ab-e38bd8a3a294",
                    "name": "OVO",
                    "image_path": "/setara-api-service/images/ewallets/OVO.png"
                  },
                  {
                    "id": "d162a822-c021-4206-b17f-8040710b5efd",
                    "name": "Dana",
                    "image_path": "/setara-api-service/images/ewallets/Dana.png"
                  }
                ]
              }"""
          )
        )
      )
    },
    security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<BaseResponse<List<EwalletResponse>>> getAllEwallets() {
    log.info("Request to fetch all E-Wallets");

    return ResponseEntity.ok(BaseResponse.success("OK", ewalletService.fetchAll()));
  }

  @GetMapping(
    value = "/banks",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Get all bank info",
    description = "Retrieves a list of all bank info",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(
            value = """
              {
                "success": true,
                "message": "OK",
                "data": [
                  {
                    "id": "dc36dbc4-d4dd-4a53-9b44-c0e7a78f08f6",
                    "name": "Tahapan BCA"
                  },
                  {
                    "id": "e292f250-1a15-44fb-8301-774f56fc54b8",
                    "name": "Bank Mandiri"
                  },
                  {
                    "id": "145e650f-0490-4b80-8ee5-14d4b68018fa",
                    "name": "Bank BNI"
                  }
                ]
              }"""
          )
        )
      )
    },
    security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<BaseResponse<List<BankResponse>>> getAllBanks() {
    log.info("Request to fetch all banks");

    return ResponseEntity.ok(BaseResponse.success("OK", bankService.fetchAll()));
  }

  @GetMapping(
    value = "/merchants/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<MerchantResponse>> getMerchantById(@ValidUUID @PathVariable("id") String id) {
    log.info("Request to fetch merchant by id: {}", id);

    UUID merchantId = UUID.fromString(id);

    return ResponseEntity.ok(BaseResponse.success("OK", merchantService.fetchById(merchantId)));
  }

}
