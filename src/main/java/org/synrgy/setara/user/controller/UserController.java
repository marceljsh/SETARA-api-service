package org.synrgy.setara.user.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.dto.SearchNoEwalletRequest;
import org.synrgy.setara.user.dto.SearchResponse;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.service.EwalletUserService;
import org.synrgy.setara.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final EwalletUserService ewalletUserService;

    @GetMapping("/getBalance")
    public ResponseEntity<BaseResponse<UserBalanceResponse>> getBalance(User user) {
        UserBalanceResponse userBalanceResponse = userService.getBalance(user);
        BaseResponse<UserBalanceResponse> response = BaseResponse.success(HttpStatus.OK, userBalanceResponse, "Success Get Balance");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-no-rek/{no}")
    public ResponseEntity<BaseResponse<SearchResponse>> searchNoRek(@Parameter(schema = @Schema(type = "string", example = "1122334455")) @PathVariable String no) {
        SearchResponse userResponse = userService.searchUserByNorek(no);
        BaseResponse<SearchResponse> response = BaseResponse.success(HttpStatus.OK, userResponse, "Success Get No Rekening");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search-no-ewallet")
    public ResponseEntity<BaseResponse<SearchResponse>> searchNoEwallet(@RequestBody SearchNoEwalletRequest request) {
        SearchResponse userResponse = ewalletUserService.searchEwalletUser(request);
        BaseResponse<SearchResponse> response = BaseResponse.success(HttpStatus.OK, userResponse, "Success Get Ewallet");
        return ResponseEntity.ok(response);
    }

}
