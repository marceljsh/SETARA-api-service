package org.synrgy.setara.contact.service;

import org.synrgy.setara.contact.dto.FavoriteEwalletRequest;
import org.synrgy.setara.contact.dto.FavoriteResponse;
import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.dto.SavedEwalletUserResponse;
import org.synrgy.setara.user.model.User;

import java.util.UUID;

public interface SavedEwalletUserService {
    void seedSavedEwalletUsers();

    SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse> getSavedEwalletUsers(User user, String ewalletName);

    FavoriteResponse putFavoriteEwalletUser(FavoriteEwalletRequest request);
}
