package org.synrgy.setara.contact.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.contact.model.SavedAccount;

@Data
@Builder
public class SavedAccountResponse {

  private String name;

  private String accountNumber;

  private String imagePath;

  private boolean favorite;

  public static SavedAccountResponse from(SavedAccount sa) {
    return SavedAccountResponse.builder()
        .name(sa.getName())
        .accountNumber(sa.getAccountNumber())
        .imagePath(sa.getImagePath())
        .favorite(sa.isFavorite())
        .build();
  }

}
