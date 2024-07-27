package org.synrgy.setara.vendor.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.vendor.dto.MerchantRequest;
import org.synrgy.setara.vendor.dto.MerchantResponse;
import org.synrgy.setara.vendor.service.MerchantService;
import org.synrgy.setara.common.dto.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/merchants")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    @GetMapping("/qris")
    public ResponseEntity<BaseResponse<MerchantResponse>> getQrisData(
            @RequestParam String id_qris,
            HttpServletRequest request) {
        MerchantRequest requestDTO = new MerchantRequest();
        requestDTO.setId_qris(id_qris);

        BaseResponse<MerchantResponse> response = merchantService.getQrisData(requestDTO);
        return new ResponseEntity<>(response, response.getCode() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

}
