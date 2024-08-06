package org.synrgy.setara.contact.service;

import org.synrgy.setara.contact.dto.FavoriteResponse;
import org.synrgy.setara.contact.dto.SavedAccountResponse;
import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.model.SavedAccount;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SavedAccountService {

  SavedEwalletAndAccountFinalResponse<SavedAccountResponse> getSavedAccounts(User user);

  FavoriteResponse putFavoriteAccount(UUID idTersimpan, boolean isFavorite);
}
