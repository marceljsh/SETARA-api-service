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
  public BankContactResponse save(User owner, BankContactAddRequest request) {
    log.trace("Creating bank contact {} for User({})",
        request.getName(), owner);

    Bank bank = bankRepo.findById(request.getBankId()).orElse(null);
    if (bank == null) {
      log.error("Bank({}) not found", request.getBankId());
      throw new EntityNotFoundException(Constants.BANK_NOT_FOUND);
    }

    BankContact bankContact = BankContact.builder()
        .owner(owner)
        .bank(bank)
        .name(request.getName())
        .accountNumber(request.getAccountNumber())
        .imagePath(request.getImagePath())
        .favorite(request.isFavorite())
        .build();

    log.trace("Saving bank contact {} of {} for User({})",
        bankContact.getName(), bankContact.getBank().getName(), owner.getId());
    return BankContactResponse.from(bcRepo.save(bankContact));
  }

  @Override
  @Transactional(readOnly = true)
  public List<BankContactResponse> fetchByOwner(User owner, boolean favOnly) {
    log.trace("Fetching {} bank contacts of User({})",
        owner.getId(), favOnly ? "favorite" : "all");

    List<BankContact> bankContacts = bcRepo.fetchAllByOwner(owner, false);

    log.trace("Fetched {}{} bank contacts of User({})",
        bankContacts.size(), favOnly ? "favorite " : "", owner.getId());

    return bankContacts.stream()
        .map(BankContactResponse::from)
        .toList();
  }

  @Override
  @Transactional
  public void updateFavorite(User owner, UUID id, boolean favorite) {
    log.trace("Updating favorite of BankContact({}) to {}",
        id, favorite);

    if (bcRepo.existsById(id) && bcRepo.belongsToOwner(owner, id)) {
      log.trace("Proceeding to update favorite of BankContact({}) to {}",
        id, favorite);
      bcRepo.updateFavorite(id, favorite);
    }
  }

  @Override
  @Transactional
  public void archive(UUID id) {
    log.trace("Archiving BankContact({})", id);

    if (bcRepo.existsById(id)) {
      log.trace("Proceeding to archive BankContact({})", id);
      bcRepo.archiveById(id);
    }
  }

  @Override
  @Transactional
  public void restore(UUID id) {
    log.trace("Restoring BankContact({})", id);

    if (bcRepo.existsById(id)) {
      log.trace("Proceeding to restore BankContact({})", id);
      bcRepo.restoreById(id);
    }
  }

}
