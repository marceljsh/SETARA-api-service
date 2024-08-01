package org.synrgy.setara.contact.controller;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.contact.dto.FavoriteRequest;
import org.synrgy.setara.contact.dto.FavoriteResponse;
import org.synrgy.setara.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.synrgy.setara.contact.dto.SavedAccountResponse;
import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.service.SavedAccountService;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SavedAccountController {

  private final SavedAccountService saService;

  @GetMapping("/saved-accounts")
  public ResponseEntity<BaseResponse<SavedEwalletAndAccountFinalResponse<SavedAccountResponse>>> getSavedAccounts() {
    SavedEwalletAndAccountFinalResponse<SavedAccountResponse> savedAccounts = saService.getSavedAccounts();
    BaseResponse<SavedEwalletAndAccountFinalResponse<SavedAccountResponse>> response = BaseResponse.success(HttpStatus.OK, savedAccounts, "Success Get Saved Accounts");
    return ResponseEntity.ok(response);
  }

  @Operation(
          summary = "Update Favorite Account",
          requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  content = @Content(
                          mediaType = MediaType.APPLICATION_JSON_VALUE,
                          schema = @Schema(implementation = FavoriteRequest.class),
                          examples = @ExampleObject(
                                  name = "Example Request",
                                  value = "{\"isFavorite\": true, \"idTersimpan\": \"1234\"}"
                          )
                  )
          )
  )
  @PutMapping("/favorite-account")
  public ResponseEntity<BaseResponse<FavoriteResponse>> putFavoriteAccount(@RequestBody FavoriteRequest request) {
    FavoriteResponse favoriteResponse = saService.putFavoriteAccount(request.getIdTersimpan(), request.isFavorite());
    BaseResponse<FavoriteResponse> response = BaseResponse.success(HttpStatus.OK, favoriteResponse, "Success update is favorite account");
    return ResponseEntity.ok(response);
  }
}
