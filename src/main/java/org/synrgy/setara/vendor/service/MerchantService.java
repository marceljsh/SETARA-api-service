package org.synrgy.setara.vendor.service;

import org.synrgy.setara.vendor.dto.MerchantRequest;
import org.synrgy.setara.vendor.dto.MerchantResponse;

public interface MerchantService {
    void seedMerchant();

    BaseResponse<MerchantResponse> getQrisData(MerchantRequest requestDTO);
}
