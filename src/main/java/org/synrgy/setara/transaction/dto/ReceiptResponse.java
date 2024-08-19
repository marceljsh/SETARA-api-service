package org.synrgy.setara.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponse {
    private String referenceNumber;
    private String dateTime;
    private String transactionType;
    private String recipientName;
    private String recipientNumber;
    private String amount;
    private String adminFee;
    private String total;
    private String status;
}
