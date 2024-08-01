package org.synrgy.setara.contact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FavoriteRequest {
    @Schema(example = "true")
    private boolean isFavorite;

    @Schema(example = "5cda13a6-8350-473a-8b71-90cddda286af")
    private UUID idTersimpan;
}
