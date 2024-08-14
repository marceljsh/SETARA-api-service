package org.synrgy.setara.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class QRPaymentRequest {

  @org.hibernate.validator.constraints.UUID(
    message = "Merchant ID must be a valid UUID"
  )
  private UUID merchantId;

  @DecimalMin("1")
  private BigDecimal amount;

  private String note;

  @NotEmpty(
    message = "MPIN is required"
  )
  private String mpin;

}
