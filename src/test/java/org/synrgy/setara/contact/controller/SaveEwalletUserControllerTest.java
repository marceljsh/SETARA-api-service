package org.synrgy.setara.contact.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.dto.SavedEwalletUserResponse;
import org.synrgy.setara.contact.service.SavedEwalletUserService;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.common.dto.BaseResponse;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class SaveEwalletUserControllerTest {
    @Mock
    private SavedEwalletUserService savedEwalletUserService;

    @InjectMocks
    private SavedEwalletUserController savedEwalletUserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSavedEwallets_Success() {
        User user = new User();
        user.setId(UUID.randomUUID());

        SavedEwalletUserResponse favoriteEwalletUserResponse = SavedEwalletUserResponse.builder()
                .id(UUID.randomUUID())
                .build();

        SavedEwalletUserResponse nonFavoriteEwalletUserResponse = SavedEwalletUserResponse.builder()
                .id(UUID.randomUUID())
                .build();

        SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse> finalResponse = new SavedEwalletAndAccountFinalResponse<>(
                1, // totalFavorites
                1, // totalSaved
                List.of(favoriteEwalletUserResponse),
                List.of(nonFavoriteEwalletUserResponse)
        );

        when(savedEwalletUserService.getSavedEwalletUsers(user, "Ovo"))
                .thenReturn(finalResponse);

        ResponseEntity<BaseResponse<SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse>>> response = savedEwalletUserController.getSavedEwallets(user, "Ovo");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success Get Saved E-Wallets", response.getBody().getMessage());

        SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse> responseData = response.getBody().getData();
        assertEquals(1, responseData.getTotalFavorites());
        assertEquals(1, responseData.getTotalSaved());
    }
}
