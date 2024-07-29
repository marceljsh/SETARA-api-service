package org.synrgy.setara.contact.dto;

import org.synrgy.setara.contact.model.EwalletContact;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SavedEwalletUserResponse {
    private UUID id;
    private UUID ownerId;
    private UUID ewalletUserId;
    private boolean favorite;
    private String ewalletUserName;
    private String ewalletUserImagePath;
    private String ewalletUserPhoneNumber;
    private String ewalletName;

    public static SavedEwalletUserResponse from(EwalletContact savedUser) {
        return SavedEwalletUserResponse.builder()
                .id(savedUser.getId())
                .ownerId(savedUser.getOwner().getId())
                .ewalletUserId(savedUser.getEwalletUser().getId())
                .favorite(savedUser.isFavorite())
                .ewalletUserName(savedUser.getEwalletUser().getName())
                .ewalletUserImagePath(savedUser.getEwalletUser().getImagePath())
                .ewalletUserPhoneNumber(savedUser.getEwalletUser().getPhoneNumber())
                .ewalletName(savedUser.getEwalletUser().getEwallet().getName())
                .build();
    }
}
