package org.synrgy.setara.contact.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.contact.dto.SavedAccountResponse;
import org.synrgy.setara.contact.model.SavedAccount;
import org.synrgy.setara.contact.repository.SavedAccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavedAccountServiceImpl implements SavedAccountService {

  private final SavedAccountRepository saRepo;

  @Override
  @Transactional
  public List<SavedAccountResponse> getSavedAccounts(UUID ownerId, boolean favOnly) {
    return saRepo.fetchAll(ownerId, favOnly)
        .stream()
        .map(SavedAccountResponse::from)
        .toList();
  }

  @Override
  @Transactional
  public SavedAccount putFavoriteAccount(UUID idTersimpan, boolean isFavorite) {
    Optional<SavedAccount> optionalSavedAccount = saRepo.findById(idTersimpan);
    if (optionalSavedAccount.isPresent()) {
      SavedAccount savedAccount = optionalSavedAccount.get();
      savedAccount.setFavorite(isFavorite);
      return saRepo.save(savedAccount);
    } else {
      throw new EntityNotFoundException("SavedAccount with id " + idTersimpan + " not found");
    }
  }
}
