package org.synrgy.setara.transaction.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MutationDatasetResponse {
    String dateAndTime;
    String description;
    String nominal;
}
