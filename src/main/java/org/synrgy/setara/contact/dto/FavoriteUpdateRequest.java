package org.synrgy.setara.contact.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteUpdateRequest {

    private boolean favorite;

}
