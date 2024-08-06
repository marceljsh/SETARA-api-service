package org.synrgy.setara.contact.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.app.util.Constants;
import org.synrgy.setara.contact.dto.EwalletContactAddRequest;
import org.synrgy.setara.contact.dto.EwalletContactResponse;
import org.synrgy.setara.contact.model.EwalletContact;
import org.synrgy.setara.contact.repository.EwalletContactRepository;
import org.synrgy.setara.user.exception.UserNotFoundException;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EwalletContactServiceImpl implements EwalletContactService {

  private final Logger log = LoggerFactory.getLogger(EwalletContactServiceImpl.class);

  private final EwalletContactRepository ecRepo;

  private final EwalletUserRepository euRepo;

  private final EwalletRepository ewalletRepo;

  private final UserRepository userRepo;

  private void createEwalletContact(User owner, EwalletUser user) {
    if (!ecRepo.existsByOwnerAndEwalletUser(owner, user)) {
      EwalletContact contact = EwalletContact.builder()
        .owner(owner)
        .ewalletUser(user)
        .name(user.getName())
        .favorite(user.getName().equals("Jermaine Cole"))
        .build();

      ecRepo.save(contact);
      log.info("Saved EwalletContact for '{}', favorite: {}", user.getName(), contact.isFavorite());
    } else {
      log.info("EwalletContact already exists for '{}' and owner '{}'", user.getName(), owner.getName());
    }
  }

  @Override
  @Transactional
  public void populate() {
    if (euRepo.count() == 0) {
      log.trace("No EwalletUser found, skipping EwalletContact seeding");
      return;
    }

    User owner = userRepo.findByName("Kendrick Lamar").orElseThrow(() -> {
      log.error("User not found: Kendrick Lamar");
      return new UserNotFoundException(Constants.ERR_USER_NOT_FOUND);
    });

    List<EwalletUser> users = euRepo.findAll();
    users.forEach(user -> createEwalletContact(owner, user));
  }

  @Override
  @Transactional
  public EwalletContactResponse save(User owner, EwalletContactAddRequest request) {
    log.trace("Creating ewallet contact {} for User({})",
        request.getName(), owner.getId());

    EwalletUser eu = euRepo.findById(request.getEwalletUserId()).orElse(null);
    if (eu == null) {
      log.error("EwalletUser({}) not found", request.getEwalletUserId());
      throw new EntityNotFoundException(Constants.ERR_EWALLET_USER_NOT_FOUND);
    }

    EwalletContact contact = EwalletContact.builder()
        .name(request.getName())
        .owner(owner)
        .ewalletUser(eu)
        .favorite(request.isFavorite())
        .build();

    log.info("Saving ewallet contact {} of {} for User({})",
        contact.getName(), contact.getEwalletUser().getEwallet().getName(), owner.getId());

    return EwalletContactResponse.from(ecRepo.save(contact));
  }

  @Override
  @Transactional(readOnly = true)
  public List<EwalletContactResponse> fetchByOwnerAndEwalletId(User owner, UUID ewalletId, boolean favOnly) {
    log.trace("Fetching ewallet contacts (fav={}) of User({}) for Ewallet({})",
        favOnly, owner.getId(), ewalletId);

    Ewallet ewallet = ewalletRepo.findById(ewalletId).orElse(null);
    if (ewallet == null) {
      log.error("Ewallet({}) not found", ewalletId);
      throw new EntityNotFoundException(Constants.ERR_EWALLET_NOT_FOUND);
    }

    List<EwalletContact> contacts = ecRepo.fetchAllByOwnerAndEwallet(owner, ewallet, favOnly);

    log.info("Fetched {} ewallet contacts (fav={}) of User({}) for Ewallet({})",
        contacts.size(), favOnly, owner.getId(), ewalletId);

    return contacts.stream()
        .map(EwalletContactResponse::from)
        .toList();
  }

  @Override
  @Transactional
  public void updateFavorite(User owner, UUID id, boolean favorite) {
    log.trace("Updating favorite of EwalletContact({}) to {}",
        id, favorite);

    if (ecRepo.existsById(id) && ecRepo.belongsToOwner(owner, id)) {
      log.info("Proceeding to update favorite of EwalletContact({}) to {}",
          id, favorite);
      ecRepo.updateFavorite(id, favorite);
    }
  }

  @Override
  @Transactional
  public void archive(UUID id) {
    log.trace("Archiving EwalletContact({})", id);

    if (ecRepo.existsById(id)) {
      log.info("Proceeding to archive EwalletContact({})", id);
      ecRepo.archiveById(id);
    }
  }

  @Override
  @Transactional
  public void restore(UUID id) {
    log.trace("Restoring EwalletContact({})", id);

    if (ecRepo.existsById(id)) {
      log.info("Proceeding to restore EwalletContact({})", id);
      ecRepo.restoreById(id);
    }
  }

}
