package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.transaction.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class QRPaymentResponse {

  private String merchantName;

  private LocalDateTime transactionTime;

  private String referenceNumber;

  private String uniqueCode;

  private BigDecimal amount;

  private String note;

  public static QRPaymentResponse from(Transaction tx) {
    return QRPaymentResponse.builder()
        .merchantName(tx.getMerchant().getName())
        .transactionTime(tx.getTransactionTime())
        .referenceNumber(tx.getReferenceNumber())
        .uniqueCode(tx.getUniqueCode())
        .amount(tx.getAmount())
        .note(tx.getNote())
        .build();
  }

}
