package org.synrgy.setara.user.service;

import org.synrgy.setara.user.dto.SearchNoEwalletRequest;
import org.synrgy.setara.user.dto.SearchResponse;

public interface EwalletUserService {
    void seedEwalletUsers();

    SearchResponse searchEwalletUser(SearchNoEwalletRequest request);
}
