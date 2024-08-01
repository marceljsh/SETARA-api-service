package org.synrgy.setara.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/getBalance")
    public ResponseEntity<BaseResponse<UserBalanceResponse>> getBalance() {
        UserBalanceResponse userBalanceResponse = userService.getBalance();
        BaseResponse<UserBalanceResponse> response = BaseResponse.success(HttpStatus.OK, userBalanceResponse, "Success Get Balance");
        return ResponseEntity.ok(response);
    }
}
