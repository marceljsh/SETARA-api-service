package org.synrgy.setara.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
import org.synrgy.setara.common.dto.BaseResponse;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final Logger log = LoggerFactory.getLogger(AuthController.class);
  private final AuthenticationManager authManager;
  private final AuthService authService;

  @Operation(
          requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  content = @Content(
                          mediaType = MediaType.APPLICATION_JSON_VALUE,
                          examples = {
                                  @ExampleObject(
                                          name = "Kendrick",
                                          value = "{\"signature\": \"KDOT604T\", \"password\": \"itsjustbigme\"}"
                                  ),
                                  @ExampleObject(
                                          name = "Jane",
                                          value = "{\"signature\": \"JANE1234\", \"password\": \"jane123\"}"
                                  ),
                                  @ExampleObject(
                                          name = "John",
                                          value = "{\"signature\": \"JOHN5678\", \"password\": \"john123\"}"
                                  ),
                                  @ExampleObject(
                                          name = "Andhika",
                                          value = "{\"signature\": \"ADTP604T\", \"password\": \"andika12345\"}"
                                  )
                          }
                  )
          )
  )
  @PostMapping(
          value = "/sign-in",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<AuthResponse>> signIn(@RequestBody LoginRequest request) {
    authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getSignature(), request.getPassword()));

    AuthResponse body = authService.authenticate(request);

    BaseResponse<AuthResponse> response = BaseResponse.success(
            HttpStatus.OK, body, "Authentication successful");

    return ResponseEntity.ok(response);
  }
}