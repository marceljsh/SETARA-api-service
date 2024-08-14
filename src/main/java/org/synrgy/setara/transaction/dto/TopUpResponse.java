package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.transaction.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TopUpResponse {

  private String referenceNumber;

  private LocalDateTime transactionTime;

  private String ewalletName;

  private String phoneNumber;

  private String name;

  private BigDecimal nominal;

  private BigDecimal adminFee;

  public static TopUpResponse of(Transaction tx, String ewalletUserName) {
    return TopUpResponse.builder()
        .referenceNumber(tx.getReferenceNumber())
        .transactionTime(tx.getTransactionTime())
        .ewalletName(tx.getEwallet().getName())
        .phoneNumber(tx.getDestPhoneNumber())
        .name(ewalletUserName)
        .nominal(tx.getAmount())
        .adminFee(tx.getAdminFee())
        .build();
  }

}
