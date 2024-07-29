package org.synrgy.setara.user.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.dto.SearchResponse;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.service.EwalletUserService;
import org.synrgy.setara.user.service.UserService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final EwalletUserService ewalletUserService;

    @GetMapping("/getBalance")
    public ResponseEntity<BaseResponse<UserBalanceResponse>> getBalance(@RequestHeader("Authorization") String token) {
        String authToken = token.substring(7);
        UserBalanceResponse userBalanceResponse = userService.getBalance(authToken);
        BaseResponse<UserBalanceResponse> response = BaseResponse.success(HttpStatus.OK, userBalanceResponse, "Success Get Balance");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-no-rek/{no}")
    public ResponseEntity<BaseResponse<SearchResponse>> searchNoRek(@RequestHeader("Authorization") String token, @PathVariable String no) {
        String authToken = token.substring(7);
        SearchResponse userResponse = userService.searchUserByNorek(no);
        BaseResponse<SearchResponse> response = BaseResponse.success(HttpStatus.OK, userResponse, "Success Get No Rekening");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-no-ewallet/{no}")
    public ResponseEntity<BaseResponse<SearchResponse>> searchNoEwallet(@RequestHeader("Authorization") String token, @PathVariable String no, @RequestBody Map<String, Object> request) {
        String authToken = token.substring(7);
        SearchResponse userResponse = ewalletUserService.searchEwalletUser(no, (String) request.get("ewallet"));
        BaseResponse<SearchResponse> response = BaseResponse.success(HttpStatus.OK, userResponse, "Success Get Ewallet");
        return ResponseEntity.ok(response);
    }

}
