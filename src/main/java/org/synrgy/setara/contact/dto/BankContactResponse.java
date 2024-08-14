package org.synrgy.setara.contact.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.contact.model.BankContact;
import org.synrgy.setara.user.dto.UserProfileResponse;
import org.synrgy.setara.vendor.dto.BankResponse;

import java.util.UUID;

@Data
@Builder
public class BankContactResponse {

  private UUID id;
  private UserProfileResponse owner;
  private BankResponse bank;
  private String name;
  private String accountNumber;
  private String imagePath;
  private boolean favorite;

  public static BankContactResponse from(BankContact bankContact) {
    return BankContactResponse.builder()
        .id(bankContact.getId())
        .owner(UserProfileResponse.from(bankContact.getOwner()))
        .bank(BankResponse.from(bankContact.getBank()))
        .name(bankContact.getName())
        .accountNumber(bankContact.getAccountNumber())
        .imagePath(bankContact.getImagePath())
        .favorite(bankContact.isFavorite())
        .build();
  }

}
