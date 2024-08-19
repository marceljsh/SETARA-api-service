package org.synrgy.setara.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MutationResponseWithPagination {
    private List<MutationResponse> mutationResponses;
    private int page;
    private int size;
    private int totalPages;
}
