package org.synrgy.setara.vendor.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.vendor.model.Bank;

import java.util.UUID;

@Data
@Builder
public class BankResponse {

  private UUID id;

  private String name;

  public static BankResponse from(Bank bank) {
    return BankResponse.builder()
        .id(bank.getId())
        .name(bank.getName())
        .build();
  }
}
