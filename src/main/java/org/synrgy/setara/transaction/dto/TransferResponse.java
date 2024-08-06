package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.transaction.model.Transaction;

import java.time.LocalDateTime;

@Data
@Builder
public class TransferResponse {

  private String referenceNumber;

  private String uniqueCode;

  private LocalDateTime transactionTime;

  private String bankName;

  private String accountNumber;

  private String note;

  public static TransferResponse from(Transaction tx) {
    return TransferResponse.builder()
        .referenceNumber(tx.getReferenceNumber())
        .uniqueCode(tx.getUniqueCode())
        .transactionTime(tx.getTransactionTime())
        .bankName(tx.getBank().getName())
        .accountNumber(tx.getDestAccountNumber())
        .note(tx.getNote())
        .build();
  }

}
