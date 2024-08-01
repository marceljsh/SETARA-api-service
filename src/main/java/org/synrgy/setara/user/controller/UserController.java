package org.synrgy.setara.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.dto.SearchResponse;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.service.EwalletUserService;
import org.synrgy.setara.user.service.UserService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final EwalletUserService ewalletUserService;

    @GetMapping("/getBalance")
    public ResponseEntity<BaseResponse<UserBalanceResponse>> getBalance() {
        UserBalanceResponse userBalanceResponse = userService.getBalance();
        BaseResponse<UserBalanceResponse> response = BaseResponse.success(HttpStatus.OK, userBalanceResponse, "Success Get Balance");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-no-rek/{no}")
    public ResponseEntity<BaseResponse<SearchResponse>> searchNoRek(@PathVariable String no) {
        SearchResponse userResponse = userService.searchUserByNorek(no);
        BaseResponse<SearchResponse> response = BaseResponse.success(HttpStatus.OK, userResponse, "Success Get No Rekening");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search-no-ewallet/{no}")
    public ResponseEntity<BaseResponse<SearchResponse>> searchNoEwallet(@PathVariable String no, @RequestBody Map<String, Object> request) {
        UUID ewalletUUID = UUID.fromString((String) request.get("ewallet"));
        SearchResponse userResponse = ewalletUserService.searchEwalletUser(no, ewalletUUID);
        BaseResponse<SearchResponse> response = BaseResponse.success(HttpStatus.OK, userResponse, "Success Get Ewallet");
        return ResponseEntity.ok(response);
    }

}
