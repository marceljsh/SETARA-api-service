package org.synrgy.setara.contact.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.contact.dto.SavedAccountResponse;
import org.synrgy.setara.contact.repository.SavedAccountRepository;

import java.util.List;
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

}
