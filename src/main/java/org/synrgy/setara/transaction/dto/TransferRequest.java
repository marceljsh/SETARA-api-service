package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TransferRequest {

  @org.hibernate.validator.constraints.UUID
  private UUID destBankId;

  private String destAccountNumber;

  private BigDecimal amount;

  private String mpin;

  private String note;

  private boolean saveContact;

}
