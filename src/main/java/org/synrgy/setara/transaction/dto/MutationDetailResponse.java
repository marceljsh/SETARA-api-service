package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MutationDetailResponse {
    private MutationUser sender;
    private MutationUser receiver;
    private BigDecimal amount;
    private BigDecimal adminFee;
    private BigDecimal totalAmount;
    private String note;

    @Data
    @Builder
    public static class MutationUser {
        private String name;
        private String accountNumber;
        private String imagePath;
        private String vendorName;
    }

}
