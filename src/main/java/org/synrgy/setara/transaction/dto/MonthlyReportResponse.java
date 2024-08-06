package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MonthlyReportResponse {

  private int month;

  private int year;

  private BigDecimal income;

  private BigDecimal expense;

  private BigDecimal gap;

  public static MonthlyReportResponse of(int month, int year, BigDecimal income, BigDecimal expense) {
    return MonthlyReportResponse.builder()
        .month(month)
        .year(year)
        .income(income)
        .expense(expense)
        .gap(income.subtract(expense).abs())
        .build();
  }

}
