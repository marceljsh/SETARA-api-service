package org.synrgy.setara.contact.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.contact.model.SavedAccount;

import java.util.UUID;

@Data
@Builder
public class SavedAccountResponse {
  private UUID id;
  private UUID ownerId;
  private boolean favorite;
  private String accountName;
  private String userImagePath;
  private String accountNumber;
  private String bankName;

  public static SavedAccountResponse from(SavedAccount savedAccount) {
    return SavedAccountResponse.builder()
            .id(savedAccount.getId())
            .ownerId(savedAccount.getOwner().getId())
            .favorite(savedAccount.isFavorite())
            .accountName(savedAccount.getName())
            .userImagePath(savedAccount.getImagePath())
            .accountNumber(savedAccount.getAccountNumber())
            .bankName(savedAccount.getBank().getName())
            .build();
  }

}
