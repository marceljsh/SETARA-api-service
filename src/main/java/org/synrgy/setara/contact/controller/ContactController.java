package org.synrgy.setara.contact.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.common.dto.ApiResponse;
import org.synrgy.setara.contact.dto.BankContactResponse;
import org.synrgy.setara.contact.dto.FavoriteUpdateRequest;
import org.synrgy.setara.contact.service.BankContactService;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ContactController {

  private final Logger log = LoggerFactory.getLogger(ContactController.class);

  private final BankContactService bcService;

  @GetMapping(
    value = "/my-bank-contacts",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> getOwnBankContacts(User owner, @RequestParam(value = "fav-only", defaultValue = "false") boolean favOnly) {
    log.info("Request to get{} bank contacts for User({})",
        favOnly ? " favorite" : "", owner.getId());

    List<BankContactResponse> bankContacts = bcService.fetchByOwner(owner, favOnly);

    return ResponseEntity.ok(ApiResponse.success("OK", bankContacts));
  }

  @PatchMapping(
    value = "/my-bank-contacts/{id}/favorite",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> toggleFavorite(User owner, @PathVariable("id") UUID id, @RequestBody FavoriteUpdateRequest request) {
    log.info("Request to update favorite status of BankContact({}) to {}",
        id, request.isFavorite());

    bcService.updateFavorite(owner, id, request.isFavorite());

    return ResponseEntity.ok(ApiResponse.success("OK", null));
  }

}
