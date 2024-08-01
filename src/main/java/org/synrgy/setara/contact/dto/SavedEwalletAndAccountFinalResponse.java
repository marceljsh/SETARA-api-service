package org.synrgy.setara.contact.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"total_favorites", "total_saved", "favorites", "saved"})
public class SavedEwalletAndAccountFinalResponse<T> {
    private long totalFavorites;
    private long totalSaved;
    private List<T> favorites;
    private List<T> saved;
}
