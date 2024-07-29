package org.synrgy.setara.vendor.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.vendor.dto.EwalletResponse;
import org.synrgy.setara.vendor.service.EwalletService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vendor")
public class EwalletController {

    private final Logger log = LoggerFactory.getLogger(EwalletController.class);
    private final EwalletService ewalletService;

    @GetMapping("/ewallets")
    public ResponseEntity<BaseResponse<List<EwalletResponse>>> getAllEwallets() {
        List<EwalletResponse> ewallets = ewalletService.getAllEwallets();
        BaseResponse<List<EwalletResponse>> response = BaseResponse.success(HttpStatus.OK, ewallets, "Success Get All E-Wallet");
        return ResponseEntity.ok(response);
    }
}
