package org.synrgy.setara.user.service;

import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.dto.UserProfileResponse;
import org.synrgy.setara.user.model.User;

public interface UserService {

  void populate();

  UserBalanceResponse fetchUserBalance(User user);
  
}
