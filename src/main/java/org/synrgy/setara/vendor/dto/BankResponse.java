package org.synrgy.setara.vendor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.vendor.model.Bank;

import java.util.UUID;

@Data
@Builder
public class BankResponse {

  @Schema(
    description = "Unique identifier for the bank",
    example = "e68a3606-49f1-4598-849e-92e67fc4aa78"
  )
  private UUID id;

  @Schema(
    description = "Name of the bank",
    example = "Tahapan BCA"
  )
  private String name;

  public static BankResponse from(Bank bank) {
    return BankResponse.builder()
        .id(bank.getId())
        .name(bank.getName())
        .build();
  }
}
