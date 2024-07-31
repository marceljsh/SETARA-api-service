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
import org.synrgy.setara.common.dto.ApiResponse;
import org.synrgy.setara.contact.dto.BankContactResponse;
import org.synrgy.setara.contact.dto.EwalletContactFetchRequest;
import org.synrgy.setara.contact.dto.EwalletContactResponse;
import org.synrgy.setara.contact.dto.FavoriteUpdateRequest;
import org.synrgy.setara.contact.service.BankContactService;
import org.synrgy.setara.contact.service.EwalletContactService;
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

  private final EwalletContactService ewService;

  @GetMapping(
    value = "/my-bank-contacts",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> getOwnBankContacts(User owner,
      @RequestParam(value = "fav-only", defaultValue = "false") boolean favOnly) {

    log.info("Request to get bank contacts (fav={}) of User({})", owner.getId(), favOnly);

    List<BankContactResponse> contacts = bcService.fetchByOwner(owner, favOnly);

    return ResponseEntity.ok(ApiResponse.success("OK", contacts));
  }

  @PatchMapping(
    value = "/my-bank-contacts/{id}/favorite",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> toggleFavoriteBankContact(User owner, @PathVariable("id") UUID id,
      @RequestBody FavoriteUpdateRequest request) {

    log.info("Request to update favorite status of BankContact({}) to {}",
        id, request.isFavorite());

    bcService.updateFavorite(owner, id, request.isFavorite());

    return ResponseEntity.ok(ApiResponse.success("OK", null));
  }

  @GetMapping(
    value = "/my-ewallet-contacts",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> getOwnEwalletContacts(User owner, @RequestBody EwalletContactFetchRequest request,
      @RequestParam(value = "fav-only", defaultValue = "false") boolean favOnly) {

    log.info("Request to fetch ewallet contacts (fav={}) of User({}) for Ewallet({})",
        favOnly, owner.getId(), request.getEwalletId());

    List<EwalletContactResponse> contacts = ewService.fetchByOwnerAndEwalletId(owner, request.getEwalletId(), favOnly);

    return ResponseEntity.ok(ApiResponse.success("OK", contacts));
  }

  @PatchMapping(
    value = "/my-ewallet-contacts/{id}/favorite",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> toggleFavoriteEwalletContact(User owner, @PathVariable("id") UUID id,
      @RequestBody FavoriteUpdateRequest request) {

    log.info("Request to update favorite status of EwalletContact({}) to {}",
        id, request.isFavorite());

    ewService.updateFavorite(owner, id, request.isFavorite());

    return ResponseEntity.ok(ApiResponse.success("OK", null));
  }

}
