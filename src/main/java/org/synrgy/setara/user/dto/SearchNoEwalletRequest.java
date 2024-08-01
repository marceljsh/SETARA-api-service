package org.synrgy.setara.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchNoEwalletRequest {
    @Schema(example = "f6e35a16-510c-4b8a-a397-adbd878e7cf2")
    private UUID ewalletId;
}
