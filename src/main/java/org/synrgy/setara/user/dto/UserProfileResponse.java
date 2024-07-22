package org.synrgy.setara.user.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.user.model.User;

@Data
@Builder
public class UserProfileResponse {

  private String name;
  private String accountNumber;
  private String signature;
  private String avatar_path;

  public static UserProfileResponse from(User user) {
    return UserProfileResponse.builder()
        .name(user.getName())
        .signature(user.getSignature())
        .accountNumber(user.getAccountNumber())
        .avatar_path(user.getImagePath())
        .build();
  }
}
