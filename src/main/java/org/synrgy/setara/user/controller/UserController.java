package org.synrgy.setara.user.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.controller.doc.GetOwnBalanceDoc;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final Logger log = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  @GetOwnBalanceDoc
  @GetMapping(
    value = "/me/balance",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<UserBalanceResponse>> getOwnBalance(@AuthenticationPrincipal User user) {
    log.info("Request to fetch balance for User({})", user.getId());

    UserBalanceResponse userBalance = userService.fetchUserBalance(user);

    return ResponseEntity.ok(BaseResponse.success("OK", userBalance));
  }

}
