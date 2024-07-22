package org.synrgy.setara.contact.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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

    public SavedEwalletUserResponse(UUID id, UUID ownerId, UUID ewalletUserId, boolean favorite,
                                    String ewalletUserName, String ewalletUserImagePath,
                                    String ewalletUserPhoneNumber, String ewalletName) {
        this.id = id;
        this.ownerId = ownerId;
        this.ewalletUserId = ewalletUserId;
        this.favorite = favorite;
        this.ewalletUserName = ewalletUserName;
        this.ewalletUserImagePath = ewalletUserImagePath;
        this.ewalletUserPhoneNumber = ewalletUserPhoneNumber;
        this.ewalletName = ewalletName;
    }
}
