package org.synrgy.setara.contact.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.dto.SavedEwalletUserResponse;
import org.synrgy.setara.contact.model.SavedEwalletUser;
import org.synrgy.setara.contact.repository.SavedEwalletUserRepository;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.vendor.model.Ewallet;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class SaveEwalletUserServiceTest {

    @Mock
    private SavedEwalletUserRepository savedEwalletUserRepo;

    @InjectMocks
    private SavedEwalletUserServiceImpl savedEwalletUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSavedEwalletUsers_Success() {
        User user = new User();
        user.setId(UUID.randomUUID());

        Ewallet ewallet = Ewallet.builder()
                .name("Ovo")
                .imagePath("path/to/image.png")
                .build();

        EwalletUser ewalletUser = new EwalletUser();
        ewalletUser.setId(UUID.randomUUID());
        ewalletUser.setEwallet(ewallet);

        SavedEwalletUser favoriteEwalletUser = SavedEwalletUser.builder()
                .owner(user)
                .ewalletUser(ewalletUser)
                .favorite(true)
                .build();

        SavedEwalletUser nonFavoriteEwalletUser = SavedEwalletUser.builder()
                .owner(user)
                .ewalletUser(ewalletUser)
                .favorite(false)
                .build();

        when(savedEwalletUserRepo.findByOwnerIdAndEwalletName(user.getId(), "Ovo"))
                .thenReturn(List.of(favoriteEwalletUser, nonFavoriteEwalletUser));

        SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse> response = savedEwalletUserService.getSavedEwalletUsers(user, "Ovo");

        assertNotNull(response);
        assertEquals(1, response.getTotalFavorites());
        assertEquals(1, response.getTotalSaved());
    }
}
