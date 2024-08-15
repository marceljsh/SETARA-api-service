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
import org.synrgy.setara.contact.controller.media.FetchEwalletContacts;
import org.synrgy.setara.contact.controller.media.ToggleFavorite;
import org.synrgy.setara.contact.dto.EwalletContactResponse;
import org.synrgy.setara.contact.dto.FavoriteUpdateRequest;
import org.synrgy.setara.contact.service.EwalletContactService;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/ewallet-contacts")
public class EwalletContactController {

  private final Logger log = LoggerFactory.getLogger(EwalletContactController.class);

  private final EwalletContactService ecService;

  @FetchEwalletContacts
  @GetMapping(
    value = "/ewallet-contacts/by-ewallet/{ewallet-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<List<EwalletContactResponse>>> getOwnContactsInEwallet(User owner,
                                                                                            @PathVariable("ewallet-id") String id,
                                                                                            @RequestParam(value = "fav-only", defaultValue = "false") boolean favOnly) {
    log.info("Request to fetch ewallet contacts (fav={}) of User(id={}) for Ewallet(id={})", favOnly, owner.getId(),
        id);

    UUID ewalletId = UUID.fromString(id);
    List<EwalletContactResponse> contacts = ecService.fetchByOwnerAndEwalletId(owner, ewalletId, favOnly);

    return ResponseEntity.ok(BaseResponse.success("OK", contacts));
  }

  @ToggleFavorite
  @PatchMapping(
    value = "/ewallet-contacts/{id}/favorite",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BaseResponse<Void>> toggleFavoriteEwalletContact(User owner, @PathVariable("id") UUID id,
                                                                         @RequestBody FavoriteUpdateRequest request) {

    log.info("Request to update favorite status of EwalletContact({}) to {}", id, request.isFavorite());

    ecService.updateFavorite(owner, id, request.isFavorite());

    return ResponseEntity.ok(BaseResponse.success("OK", null));
  }

}
