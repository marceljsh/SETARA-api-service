package org.synrgy.setara.transaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.transaction.model.MutationType;

import java.time.LocalDate;

@Data
@Builder
public class MutationRequest {

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  private MutationType type;

  private int page;

  private int size;

}
