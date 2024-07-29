package org.synrgy.setara.contact.service;

import org.synrgy.setara.contact.dto.BankContactAddRequest;
import org.synrgy.setara.contact.dto.BankContactResponse;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.UUID;

public interface BankContactService {

  BankContactResponse save(User owner, BankContactAddRequest request);

  List<BankContactResponse> fetchByOwner(User owner, boolean favOnly);

  void updateFavorite(User owner, UUID id, boolean favorite);

  void archive(UUID id);

  void restore(UUID id);

}
