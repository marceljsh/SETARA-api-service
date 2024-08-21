package org.synrgy.setara.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {
    private UUID idTransaction;
    private UserDTO sourceUser;
    private UserDTO destinationUser;
    private BigDecimal amount;
    private BigDecimal adminFee;
    private BigDecimal totalAmount;
    private String note;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO  {
        private String name;
        private String bank;
        private String accountNumber;
        private String imagePath;
    }
}
