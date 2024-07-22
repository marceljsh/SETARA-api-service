package org.synrgy.setara.vendor.service;

import org.synrgy.setara.vendor.model.Ewallet;
import java.util.List;

public interface EwalletService {
    void seedEwallet();

    List<Ewallet> getAllEwallets();
}
