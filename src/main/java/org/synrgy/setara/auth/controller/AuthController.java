package org.synrgy.setara.auth.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.auth.controller.media.AuthReq;
import org.synrgy.setara.auth.controller.media.AuthRes;
import org.synrgy.setara.auth.dto.AuthResponse;
import org.synrgy.setara.auth.dto.LoginRequest;
import org.synrgy.setara.auth.service.AuthService;
import org.synrgy.setara.common.dto.BaseResponse;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final Logger log = LoggerFactory.getLogger(AuthController.class);

  private final AuthenticationManager authManager;
  private final AuthService authService;

  @AuthRes
  @PostMapping(
    value = "/sign-in",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<AuthResponse>> signIn(@AuthReq @RequestBody LoginRequest request) {
    log.info("Request to sign in for {}", request.getSignature());

    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(request.getSignature(), request.getPassword());
    authManager.authenticate(token);

    AuthResponse body = authService.authenticate(request);
    BaseResponse<AuthResponse> response = BaseResponse.success("OK", body);

    return ResponseEntity.ok(response);
  }
}