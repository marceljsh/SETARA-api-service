package org.synrgy.setara.contact.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.app.util.Constants;
import org.synrgy.setara.contact.dto.BankContactAddRequest;
import org.synrgy.setara.contact.dto.BankContactResponse;
import org.synrgy.setara.contact.model.BankContact;
import org.synrgy.setara.contact.repository.BankContactRepository;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.vendor.exception.BankNotFoundException;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.repository.BankRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankContactServiceImpl implements BankContactService {

  private final Logger log = LoggerFactory.getLogger(BankContactServiceImpl.class);

  private final BankContactRepository bcRepo;
  private final BankRepository bankRepo;

  @Override
  @Transactional
  public BankContactResponse addBankContact(User owner, BankContactAddRequest request) {
    log.debug("Creating bank contact {} for User({})", request.getName(), owner);

    Bank bank = bankRepo.findById(request.getBankId()).orElseThrow(() -> {
      log.error("Bank({}) not found", request.getBankId());
      return new BankNotFoundException(Constants.ERR_BANK_NOT_FOUND);
    });

    BankContact contact = BankContact.builder()
        .owner(owner)
        .bank(bank)
        .name(request.getName())
        .accountNumber(request.getAccountNumber())
        .imagePath(request.getImagePath())
        .favorite(request.isFavorite())
        .build();

    log.debug("Saving bank contact {} of {} for User({})", contact.getName(),
        contact.getBank().getName(), owner.getId());

    return BankContactResponse.from(bcRepo.save(contact));
  }

  @Override
  @Transactional(readOnly = true)
  public List<BankContactResponse> fetchByOwner(User owner, boolean favOnly) {
    log.debug("Fetching bank contacts (fav={}) of User({})", favOnly, owner.getId());

    List<BankContact> contacts = bcRepo.fetchAllByOwner(owner, false);

    log.debug("Fetched {} bank contacts (fav={}) of User({})", contacts.size(), favOnly, owner.getId());

    return contacts.stream()
        .map(BankContactResponse::from)
        .toList();
  }

  @Override
  @Transactional
  public void updateFavorite(User owner, UUID id, boolean favorite) {
    log.debug("Updating favorite of BankContact({}) to {}", id, favorite);

    if (bcRepo.existsById(id) && bcRepo.belongsToOwner(owner, id)) {
      log.debug("Proceeding to update favorite of BankContact({}) to {}", id, favorite);
      bcRepo.updateFavorite(id, favorite);
    }
  }

  @Override
  @Transactional
  public void archive(UUID id) {
    log.debug("Archiving BankContact({})", id);

    if (bcRepo.existsById(id)) {
      log.debug("Proceeding to archive BankContact({})", id);
      bcRepo.archiveById(id);
    }
  }

  @Override
  @Transactional
  public void restore(UUID id) {
    log.debug("Restoring BankContact({})", id);

    if (bcRepo.existsById(id)) {
      log.debug("Proceeding to restore BankContact({})", id);
      bcRepo.restoreById(id);
    }
  }

}
