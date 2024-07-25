package org.synrgy.setara.contact.service;

import org.synrgy.setara.contact.dto.FavoriteResponse;
import org.synrgy.setara.contact.dto.SavedAccountResponse;
import org.synrgy.setara.contact.model.SavedAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SavedAccountService {

  List<SavedAccountResponse> getSavedAccounts(UUID ownerId, boolean favOnly);

  FavoriteResponse putFavoriteAccount(UUID idTersimpan, boolean isFavorite);
}
