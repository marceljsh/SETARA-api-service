package org.synrgy.setara.vendor.service;

import org.synrgy.setara.vendor.dto.MerchantResponse;

import java.util.UUID;

public interface MerchantService {
    void seedMerchant();

    MerchantResponse getQrisData(UUID idQris);
}
