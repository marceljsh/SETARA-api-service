package org.synrgy.setara.contact.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FavoriteRequest {
    private boolean isFavorite;
    private UUID idTersimpan;
}
