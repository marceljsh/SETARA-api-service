package org.synrgy.setara.transaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.transaction.model.MutationType;

import java.time.LocalDate;

@Data
@Builder
public class MutationRequest {

  @NotEmpty
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @NotEmpty
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @NotEmpty
  @Enumerated(EnumType.STRING)
  private MutationType type;

  private int page;

  private int size;

}
