package org.synrgy.setara.contact.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.contact.model.EwalletContact;
import org.synrgy.setara.user.dto.EwalletUserResponse;
import org.synrgy.setara.vendor.dto.EwalletResponse;

import java.util.UUID;

@Data
@Builder
public class EwalletContactResponse {

  private UUID id;
  private EwalletResponse ewallet;
  private String name;
  private String phoneNumber;
  private String imagePath;
  private boolean favorite;

  public static EwalletContactResponse from(EwalletContact contact) {
    return EwalletContactResponse.builder()
        .id(contact.getId())
        .ewallet(EwalletResponse.from(contact.getEwalletUser().getEwallet()))
        .name(contact.getEwalletUser().getName())
        .phoneNumber(contact.getEwalletUser().getPhoneNumber())
        .imagePath(contact.getEwalletUser().getImagePath())
        .favorite(contact.isFavorite())
        .build();
  }

}
