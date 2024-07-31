package org.synrgy.setara.vendor.service;

import org.synrgy.setara.vendor.dto.BankResponse;

import java.util.List;

public interface BankService {

    void populate();

    List<BankResponse> fetchAll();

}
