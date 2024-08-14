package org.synrgy.setara.contact.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.app.util.Regexp;

import java.util.UUID;

@Data
@Builder
public class BankContactAddRequest {

  @org.hibernate.validator.constraints.UUID(
    message = "Bank ID must be a valid UUID"
  )
  private UUID bankId;

  @Size(
    min = 3,
    max = 64,
    message = "Name must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = Regexp.DISPLAY_NAME,
    message = "Name can only have letters, numbers, and spaces"
  )
  private String name;

  @Size(
    min = 10,
    max = 20,
    message = "Account number must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = Regexp.BANK_ACCOUNT_NUMBER,
    message = "Account number can only have numbers"
  )
  private String accountNumber;

  private String imagePath;

  private boolean favorite;

}
