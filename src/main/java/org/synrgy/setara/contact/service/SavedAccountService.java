package org.synrgy.setara.contact.service;

import org.synrgy.setara.contact.dto.SavedAccountResponse;

import java.util.List;
import java.util.UUID;

public interface SavedAccountService {

  List<SavedAccountResponse> getSavedAccounts(UUID ownerId, boolean favOnly);

}
