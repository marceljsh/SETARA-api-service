package org.synrgy.setara.contact.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.contact.dto.PutFavoriteRequest;
import org.synrgy.setara.contact.dto.SavedAccountResponse;
import org.synrgy.setara.contact.model.SavedAccount;
import org.synrgy.setara.contact.service.SavedAccountService;
import org.synrgy.setara.contact.service.SavedEwalletUserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ContactController {

  private final Logger log = LoggerFactory.getLogger(ContactController.class);

  private final SavedAccountService saService;

  private final SavedEwalletUserService sewuService;

  /* Saved Account section */
  @GetMapping(
    value = "/saved-accounts",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<SavedAccountResponse>> getSavedAccounts(@RequestParam(value = "fav-only", defaultValue = "false") boolean favOnly) {
    // dummy ownerId, in future use interceptor to get from token
    UUID ownerId = UUID.randomUUID();

    log.info("Fetching saved accounts, owner_id=[{}], fav_only=[{}]", ownerId, favOnly);

    return ResponseEntity.ok(saService.getSavedAccounts(ownerId, favOnly));
  }

  /* Saved Ewallet User section */
  // use prefix "/saved-ewallet-users"

  @PutMapping("/favorite-account")
  public ResponseEntity<BaseResponse<SavedAccount>> putFavoriteAccount(@RequestBody PutFavoriteRequest request) {
    try {
      SavedAccount savedAccount = saService.putFavoriteAccount(request.getIdTersimpan(), request.isFavorite());
      BaseResponse<SavedAccount> response = BaseResponse.success(savedAccount, "Success update is favorite account");
      return ResponseEntity.ok(response);
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(BaseResponse.failure(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
  }
}
