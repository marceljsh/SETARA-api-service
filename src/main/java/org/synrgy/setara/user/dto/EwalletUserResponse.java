package org.synrgy.setara.user.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.vendor.dto.EwalletResponse;

import java.util.UUID;

@Data
@Builder
public class EwalletUserResponse {

  private UUID id;

  private EwalletResponse ewallet;

  private String name;

  private String phoneNumber;

  private String imagePath;

  public static EwalletUserResponse from(EwalletUser user) {
    return EwalletUserResponse.builder()
        .id(user.getId())
        .ewallet(EwalletResponse.from(user.getEwallet()))
        .name(user.getName())
        .phoneNumber(user.getPhoneNumber())
        .imagePath(user.getImagePath())
        .build();
  }

}
