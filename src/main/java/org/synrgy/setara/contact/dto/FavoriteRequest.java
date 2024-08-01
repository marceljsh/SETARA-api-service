package org.synrgy.setara.contact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FavoriteRequest {
    @Schema(description = "isFavorite", example = "true")
    private boolean isFavorite;

    @Schema(description = "idTersimpan", example = "028296c2-1cfc-4995-9e89-f5cc61ef451a")
    private UUID idTersimpan;
}
