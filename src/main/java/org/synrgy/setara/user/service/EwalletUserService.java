package org.synrgy.setara.user.service;

import org.synrgy.setara.user.dto.SearchResponse;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;

import java.util.UUID;

public interface EwalletUserService {
    void seedEwalletUsers();

    SearchResponse searchEwalletUser(String no_ewallet, UUID ewallet);
}
