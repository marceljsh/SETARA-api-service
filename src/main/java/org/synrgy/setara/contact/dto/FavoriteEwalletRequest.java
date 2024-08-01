package org.synrgy.setara.contact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FavoriteEwalletRequest {
    @Schema(example = "true")
    private boolean isFavorite;

    @Schema(example = "fa32d664-6893-456a-89e8-9f05be5aa703")
    private UUID idTersimpan;
}
