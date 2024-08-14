package org.synrgy.setara.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.app.util.Regexp;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TransferRequest {

  @org.hibernate.validator.constraints.UUID(
    message = "Destination Bank ID must be a valid UUID"
  )
  private UUID destBankId;

  @Size(
    min = 10,
    max = 20,
    message = "Destination account number must be {min}-{max} characters long"

  )
  @Pattern(
    regexp = Regexp.BANK_ACCOUNT_NUMBER,
    message = "Destination account number can only have numbers"
  )
  private String destAccountNumber;

  @DecimalMin("10000")
  private BigDecimal amount;

  @NotEmpty(
    message = "MPIN is required"
  )
  private String mpin;

  private String note;

  private String name;

  private boolean saveContact;

}
