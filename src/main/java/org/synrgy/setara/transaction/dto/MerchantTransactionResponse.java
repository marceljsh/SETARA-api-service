package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MerchantTransactionResponse {
    private String idTransaction;
    private SourceUserDTO sourceUser;
    private DestinationUserDTO destinationUser;
    private BigDecimal amount;
    private BigDecimal adminFee;
    private BigDecimal totalAmount;
    private String note;

    @Data
    @Builder
    public static class SourceUserDTO {
        private String name;
        private String bank;
        private String accountNumber;
        private String imagePath;
    }

    @Data
    @Builder
    public static class DestinationUserDTO {
        private String name;
        private String nameMerchant;
        private String nmid;
        private String terminalId;
        private String imagePath;
    }
}
