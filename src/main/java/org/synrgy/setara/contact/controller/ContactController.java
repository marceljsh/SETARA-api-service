package org.synrgy.setara.contact.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.synrgy.setara.contact.dto.SavedAccountResponse;
import org.synrgy.setara.contact.service.SavedAccountService;
import org.synrgy.setara.contact.service.SavedEwalletUserService;
import org.synrgy.setara.user.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ContactController {

  private final Logger log = LoggerFactory.getLogger(ContactController.class);

  private final SavedAccountService saService;

  private final SavedEwalletUserService sewuService;

  private final UserRepository userRepo;

  /* Saved Account section */
  @GetMapping(
    value = "/saved-accounts",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<SavedAccountResponse>> getSavedAccounts(@RequestParam(value = "fav-only", defaultValue = "false") boolean favOnly) {
    // TODO: use argument resolver instead
    String signature = SecurityContextHolder.getContext().getAuthentication().getName();
    UUID ownerId = userRepo.findBySignature(signature).get().getId();

    log.info("Fetching saved accounts, owner_id=[{}], fav_only=[{}]", ownerId, favOnly);

    return ResponseEntity.ok(saService.getSavedAccounts(ownerId, favOnly));
  }

  /* Saved Ewallet User section */
  // use prefix "/saved-ewallet-users"

}
