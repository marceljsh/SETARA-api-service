package org.synrgy.setara.vendor.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.vendor.dto.MerchantResponse;
import org.synrgy.setara.vendor.service.MerchantService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/qris/{id_qris}")
    public ResponseEntity<BaseResponse<MerchantResponse>> getQrisData(@Parameter(schema = @Schema(example = "e56192b9-d09c-4927-b0e2-ae1e60f1e427")) @PathVariable("id_qris") UUID idQris) {
        MerchantResponse response = merchantService.getQrisData(idQris);
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, response,"Get qris data successful"));
    }
}
