package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Data
@Builder
public class TopUpRequest {
    private UUID idEwallet;
    private String destinationPhoneNumber;
    private BigDecimal amount;
    private String mpin;
    private String note;
    private boolean savedAccount;
}
