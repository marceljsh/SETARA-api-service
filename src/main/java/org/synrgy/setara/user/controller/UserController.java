package org.synrgy.setara.user.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.common.dto.ApiResponse;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.dto.UserProfileResponse;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final Logger log = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  @GetMapping("/get-own-balance")
  public ResponseEntity<ApiResponse<Object>> getOwnBalance(User user) {
    UserBalanceResponse userBalance = userService.fetchUserBalance(user);
    return ResponseEntity.ok(ApiResponse.success("OK", userBalance));
  }

  @GetMapping("/by-acc-no/{acc-no}")
  public ResponseEntity<ApiResponse<UserProfileResponse>> fetchByAccountNumber(@PathVariable("acc-no") String accNo) {
    UserProfileResponse user = userService.searchByAccNumber(accNo);
    return ResponseEntity.ok(ApiResponse.success("OK", user));
  }

}
