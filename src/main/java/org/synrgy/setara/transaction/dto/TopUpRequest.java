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
public class TopUpRequest {

  @NotEmpty(message = "MPIN is required")
  private String mpin;

  @org.hibernate.validator.constraints.UUID(
    message = "Ewallet ID must be a valid UUID"
  )
  private UUID ewalletId;

  @Size(
    min = 9,
    max = 13,
    message = "Phone number must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = Regexp.PHONE_NUMBER,
    message = "Phone number must contain only digits"
  )
  private String phoneNumber;

  @DecimalMin("10000")
  private BigDecimal amount;

  private String note;

  private String name;

  private boolean saveContact;

}
