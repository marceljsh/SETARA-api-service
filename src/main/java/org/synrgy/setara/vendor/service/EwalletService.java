package org.synrgy.setara.vendor.service;

import java.util.List;

public interface EwalletService {
    void seedEwallet();

    List<EwalletResponse> getAllEwallets();
}
