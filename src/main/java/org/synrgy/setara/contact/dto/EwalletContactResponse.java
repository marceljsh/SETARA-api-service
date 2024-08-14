package org.synrgy.setara.contact.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.contact.model.EwalletContact;
import org.synrgy.setara.user.dto.EwalletUserResponse;
import org.synrgy.setara.user.dto.UserProfileResponse;

import java.util.UUID;

@Data
@Builder
public class EwalletContactResponse {

  private UUID id;
  private UserProfileResponse owner;
  private EwalletUserResponse ewalletUser;
  private boolean favorite;

  public static EwalletContactResponse from(EwalletContact contact) {
    return EwalletContactResponse.builder()
        .id(contact.getId())
        .owner(UserProfileResponse.from(contact.getOwner()))
        .ewalletUser(EwalletUserResponse.from(contact.getEwalletUser()))
        .favorite(contact.isFavorite())
        .build();
  }

}
