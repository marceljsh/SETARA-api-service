package org.synrgy.setara.vendor.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.vendor.dto.MerchantRequest;
import org.synrgy.setara.vendor.dto.MerchantResponse;
import org.synrgy.setara.vendor.service.MerchantService;
import org.synrgy.setara.common.dto.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchants")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    @GetMapping("/qris/{id_qris}")
    public ResponseEntity<BaseResponse<MerchantResponse>> getQrisData(
            @Parameter(schema = @Schema(example = "e56192b9-d09c-4927-b0e2-ae1e60f1e427")) @PathVariable String id_qris,
            HttpServletRequest request) {
        MerchantRequest requestDTO = new MerchantRequest();
        requestDTO.setIdQris(id_qris);

        BaseResponse<MerchantResponse> response = merchantService.getQrisData(requestDTO);
        return new ResponseEntity<>(response, response.getCode() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

}
