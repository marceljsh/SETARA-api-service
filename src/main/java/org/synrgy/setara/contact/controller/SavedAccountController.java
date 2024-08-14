package org.synrgy.setara.contact.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.contact.dto.FavoriteRequest;
import org.synrgy.setara.contact.dto.FavoriteResponse;
import org.synrgy.setara.contact.dto.SavedAccountResponse;
import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.service.SavedAccountService;
import org.synrgy.setara.user.model.User;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SavedAccountController {

  private final SavedAccountService saService;

  @GetMapping("/saved-accounts")
  public ResponseEntity<BaseResponse<SavedEwalletAndAccountFinalResponse<SavedAccountResponse>>> getSavedAccounts(
          @AuthenticationPrincipal User user) {
    SavedEwalletAndAccountFinalResponse<SavedAccountResponse> savedAccounts = saService.getSavedAccounts(user);
    BaseResponse<SavedEwalletAndAccountFinalResponse<SavedAccountResponse>> response = BaseResponse.success(HttpStatus.OK, savedAccounts, "Success Get Saved Accounts");
    return ResponseEntity.ok(response);
  }

  @PutMapping("/favorite-account")
  public ResponseEntity<BaseResponse<FavoriteResponse>> putFavoriteAccount(@RequestBody FavoriteRequest request) {
    FavoriteResponse favoriteResponse = saService.putFavoriteAccount(request.getIdTersimpan(), request.isFavorite());
    BaseResponse<FavoriteResponse> response = BaseResponse.success(HttpStatus.OK, favoriteResponse, "Success update is favorite account");
    return ResponseEntity.ok(response);
  }
}
