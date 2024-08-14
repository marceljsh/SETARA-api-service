package org.synrgy.setara.transaction.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthlyReportRequest {

  @Min(1)
  @Max(12)
  private int month;

  @Min(2001)
  private int year;

}
