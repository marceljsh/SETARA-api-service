package org.synrgy.setara.user.service;

import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;

public interface EwalletUserService {
    void seedEwalletUsers();

    EwalletUser searchEwalletUser(String no_ewallet, String ewallet);
}
