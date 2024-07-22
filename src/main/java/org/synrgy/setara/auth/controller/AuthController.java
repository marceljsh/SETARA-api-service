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
import org.synrgy.setara.auth.dto.AuthResponse;
import org.synrgy.setara.auth.dto.LoginRequest;
import org.synrgy.setara.auth.service.AuthService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final Logger log = LoggerFactory.getLogger(AuthController.class);

  private final AuthenticationManager authManager;

  private final AuthService authService;

  @PostMapping(
    value = "/sign-in",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest request) {
    log.info("Authenticating user with signature: {}", request.getSignature());

    authManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getSignature(), request.getPassword()));

    log.info("User with signature: {} has been authenticated", request.getSignature());

    AuthResponse body = authService.authenticate(request);

    log.info("User with signature: {} has been authorized", request.getSignature());

    return ResponseEntity.ok(body);
  }
}
