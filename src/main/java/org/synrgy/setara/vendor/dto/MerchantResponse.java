package org.synrgy.setara.vendor.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MerchantResponse {
    private String merchantName;
    private String name;
    private String nmid;
    private String terminalId;
    private String imagePath;
    private String address;
    private String qrisCode;
}
