package org.synrgy.setara.contact.service;

import org.synrgy.setara.contact.dto.EwalletContactAddRequest;
import org.synrgy.setara.contact.dto.EwalletContactResponse;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.UUID;

public interface EwalletContactService {

  EwalletContactResponse save(User owner, EwalletContactAddRequest request);

  List<EwalletContactResponse> fetchByOwnerAndEwalletId(User owner, UUID ewalletId, boolean favOnly);

  void updateFavorite(User owner, UUID id, boolean favorite);

  void archive(UUID id);

  void restore(UUID id);

}
