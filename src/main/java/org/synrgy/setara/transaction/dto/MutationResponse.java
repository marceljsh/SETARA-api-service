package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.transaction.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MutationResponse {

  private String referenceNumber;
  private String uniqueCode;
  private UUID transactionId;
  private String transactionType;
  private BigDecimal totalAmount;
  private LocalDateTime transactionTime;
  private String destination;

  public static MutationResponse from(Transaction tx) {
    BigDecimal totalAmount = tx.getAmount().subtract(tx.getAdminFee());

    String destination;
    switch (tx.getType()) {
      case TRANSFER -> destination = tx.getDestAccountNumber();
      case TOP_UP -> destination = tx.getDestPhoneNumber();
      case QR_PAYMENT -> destination = tx.getMerchant().getId().toString();
      default -> destination = null;
    }

    return MutationResponse.builder()
        .transactionId(tx.getId())
        .uniqueCode(tx.getUniqueCode())
        .transactionType(tx.getType().getName())
        .totalAmount(totalAmount)
        .transactionTime(tx.getTransactionTime())
        .referenceNumber(tx.getReferenceNumber())
        .destination(destination)
        .build();
  }

}
