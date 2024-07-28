package org.synrgy.setara.user.service;

import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.model.User;

public interface UserService {

  void seedUser();
  UserBalanceResponse getBalance();

  User searchUserByNorek(String acount_no, String bank);
}
