package org.synrgy.setara.vendor.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class MerchantResponse {
    private String name;
    private String nmid;
    private String terminalId;
    private String imagePath;
    private String address;
    private String qrisCode;
}
