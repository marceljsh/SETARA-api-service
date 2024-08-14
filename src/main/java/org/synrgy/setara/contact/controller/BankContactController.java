package org.synrgy.setara.contact.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.contact.dto.BankContactResponse;
import org.synrgy.setara.contact.dto.FavoriteUpdateRequest;
import org.synrgy.setara.contact.service.BankContactService;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/bank-contacts")
public class BankContactController {

  private final Logger log = LoggerFactory.getLogger(BankContactController.class);

  private final BankContactService bcService;

  @GetMapping(
    value = "/my",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> getOwnBankContacts(User owner,
      @RequestParam(value = "fav-only", defaultValue = "false") boolean favOnly) {
    log.info("Request to get bank contacts (fav={}) of User({})", owner.getId(), favOnly);

    List<BankContactResponse> contacts = bcService.fetchByOwner(owner, favOnly);

    return ResponseEntity.ok(BaseResponse.success("OK", contacts));
  }

  @PatchMapping(
    value = "/{id}/favorite",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> toggleFavoriteBankContact(User owner, @PathVariable("id") UUID id,
      @RequestBody FavoriteUpdateRequest request) {
    log.info("Request to update favorite status of BankContact(id={}) to {}", id, request.isFavorite());

    bcService.updateFavorite(owner, id, request.isFavorite());

    return ResponseEntity.ok(BaseResponse.success("OK", null));
  }

}
