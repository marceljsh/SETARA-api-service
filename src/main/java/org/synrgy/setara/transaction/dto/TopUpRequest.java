package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TopUpRequest {

  private String mpin;

  private UUID ewalletId;

  private String phoneNumber;

  private BigDecimal amount;

  private String note;

  private String name;

  private boolean saveContact;

}
