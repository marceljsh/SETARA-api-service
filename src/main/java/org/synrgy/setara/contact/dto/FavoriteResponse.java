package org.synrgy.setara.contact.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FavoriteResponse {
    private final UUID idTersimpan;
    private final boolean favorite;

    public FavoriteResponse(UUID idTersimpan, boolean favorite) {
        this.idTersimpan = idTersimpan;
        this.favorite = favorite;
    }
}
