package org.synrgy.setara.contact.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.contact.dto.FavoriteResponse;
import org.synrgy.setara.contact.dto.SavedAccountResponse;
import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.exception.SavedAccountExceptions.*;
import org.synrgy.setara.contact.exception.SavedEwalletExceptions;
import org.synrgy.setara.contact.model.SavedAccount;
import org.synrgy.setara.contact.model.SavedEwalletUser;
import org.synrgy.setara.contact.repository.SavedAccountRepository;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavedAccountServiceImpl implements SavedAccountService {

  private final SavedAccountRepository saRepo;
  private final UserRepository userRepo;

  @Override
  @Transactional
  public SavedEwalletAndAccountFinalResponse<SavedAccountResponse> getSavedAccounts() {
    String signature = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepo.findBySignature(signature)
            .orElseThrow(() -> new SavedEwalletExceptions.UserNotFoundException("User with signature " + signature + " not found"));

    List<SavedAccount> savedAccounts = saRepo.findByOwnerId(user.getId());

    List<SavedAccountResponse> favoriteAccounts = savedAccounts.stream()
            .filter(SavedAccount::isFavorite)
            .map(SavedAccountResponse::from)
            .toList();

    List<SavedAccountResponse> nonFavoriteAccounts = savedAccounts.stream()
            .filter(account -> !account.isFavorite())
            .map(SavedAccountResponse::from)
            .toList();

    long favoriteCount = favoriteAccounts.size();
    long nonFavoriteCount = nonFavoriteAccounts.size();

    return new SavedEwalletAndAccountFinalResponse<>(
            favoriteCount,
            nonFavoriteCount,
            favoriteAccounts,
            nonFavoriteAccounts
    );
  }

  @Override
  @Transactional
  public FavoriteResponse putFavoriteAccount(UUID idTersimpan, boolean isFavorite) {
    Optional<SavedAccount> optionalSavedAccount = saRepo.findById(idTersimpan);
    if (optionalSavedAccount.isPresent()) {
      SavedAccount savedAccount = optionalSavedAccount.get();
      savedAccount.setFavorite(isFavorite);
      saRepo.save(savedAccount);
      return new FavoriteResponse(idTersimpan, isFavorite);
    } else {
      throw new SavedAccountNotFoundException("SavedAccount with id " + idTersimpan + " not found");
    }
  }
}
