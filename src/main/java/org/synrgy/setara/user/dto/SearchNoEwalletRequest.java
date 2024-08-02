package org.synrgy.setara.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@Builder
public class SearchNoEwalletRequest {
    @Schema(example = "081234567890")
    private String noEwallet;

    @Schema(example = "f4fce5e1-38f8-42a6-9473-5fe563a52b01")
    private UUID ewalletId;
}
