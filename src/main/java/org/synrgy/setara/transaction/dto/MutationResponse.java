package org.synrgy.setara.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MutationResponse {
    private UUID transactionId;
    private String uniqueCode;
    private String type;
    private BigDecimal totalAmount;
    private LocalDateTime time;
    private String referenceNumber;
    private String destinationAccountNumber;
    private String destinationPhoneNumber;
    private String formattedDate;
    private String formattedTime;
}
