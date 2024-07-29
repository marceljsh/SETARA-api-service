package org.synrgy.setara.contact.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BankContactAddRequest {

  private UUID ownerId;

  private UUID bankId;

  private String name;

  private String accountNumber;

  private String imagePath;

  private boolean favorite;

}
