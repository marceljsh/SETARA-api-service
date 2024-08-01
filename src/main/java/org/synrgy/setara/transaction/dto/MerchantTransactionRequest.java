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
public class MerchantTransactionRequest {
    private UUID idQris;
    private BigDecimal amount;
    private String note;
    private String mpin;
}
