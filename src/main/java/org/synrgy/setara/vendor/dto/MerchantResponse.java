package org.synrgy.setara.vendor.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MerchantResponse {
    private String merchant_name;
    private String name;
    private String nmid;
    private String terminalId;
    private String image_path;
    private String address;
    private String qris_code;
}
