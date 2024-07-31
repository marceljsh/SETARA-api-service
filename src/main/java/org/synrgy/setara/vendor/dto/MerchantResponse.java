package org.synrgy.setara.vendor.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.vendor.model.Merchant;

import java.util.UUID;

@Data
@Builder
public class MerchantResponse {

  private UUID id;

  private String name;

  private String terminalId;

  private String nmid;

  private String address;

  private String qrisCode;

  private String imagePath;

  public static MerchantResponse from(Merchant merchant) {
    return MerchantResponse.builder()
        .id(merchant.getId())
        .name(merchant.getName())
        .terminalId(merchant.getTerminalId())
        .nmid(merchant.getNmid())
        .address(merchant.getAddress())
        .qrisCode(merchant.getQrisCode())
        .imagePath(merchant.getImagePath())
        .build();
  }

}
