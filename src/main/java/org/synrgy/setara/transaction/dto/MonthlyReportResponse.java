package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MonthlyReportResponse {
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal total;
}
