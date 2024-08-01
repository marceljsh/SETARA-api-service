package org.synrgy.setara.transaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "e56192b9-d09c-4927-b0e2-ae1e60f1e427")
    private UUID idQris;

    private BigDecimal amount;

    private String note;

    @Schema(example = "170687")
    private String mpin;
}
