package org.synrgy.setara.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopUpResponse {
    private UserDto user;
    private UserEwalletDto userEwallet;
    private BigDecimal amount;
    private BigDecimal adminFee;
    private BigDecimal totalAmount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDto {
        private String accountNumber;
        private String name;
        private String imagePath;
        private String bankName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EwalletDto {
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserEwalletDto {
        private String name;
        private String phoneNumber;
        private String imagePath;
        private EwalletDto ewallet;
    }
}
