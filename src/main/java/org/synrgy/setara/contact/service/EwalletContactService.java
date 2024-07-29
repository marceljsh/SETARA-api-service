package org.synrgy.setara.contact.service;

import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.dto.SavedEwalletUserResponse;

import java.util.UUID;

public interface EwalletContactService {
    void seedSavedEwalletUsers();

    SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse> getSavedEwalletUsers(String ewalletName);

    FavoriteResponse putFavoriteEwalletUser(UUID idTersimpan, boolean isFavorite);
}
