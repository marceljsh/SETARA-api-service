package org.synrgy.setara.contact.service;

import org.synrgy.setara.contact.dto.SavedEwalletUserResponse;
import org.synrgy.setara.user.model.User;
import java.util.List;

public interface SavedEwalletUserService {
    void seedSavedEwalletUsers();

    List<SavedEwalletUserResponse> getSavedEwalletUsersForUser(User user, Boolean favorite);
}
