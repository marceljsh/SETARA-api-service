package org.synrgy.setara.user.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class UserBalanceResponse {
    private LocalDateTime checkTime;
    private BigDecimal balance;
}
