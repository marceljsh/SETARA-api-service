package org.synrgy.setara.user.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.user.model.User;

import java.math.BigDecimal;

@Data
@Builder
public class UserProfileResponse {

  private String name;
  private String accountNumber;
  private BigDecimal balance;
  private String imagePath;

  public static UserProfileResponse from(User user) {
    return UserProfileResponse.builder()
        .name(user.getName())
        .accountNumber(user.getAccountNumber())
        .balance(user.getBalance())
        .imagePath(user.getImagePath())
        .build();
  }
}
