package org.synrgy.setara.contact.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.contact.dto.FavoriteResponse;
import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.dto.SavedEwalletUserResponse;
import org.synrgy.setara.contact.service.SavedEwalletUserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SavedEwalletUserController {
    private final Logger log = LoggerFactory.getLogger(SavedEwalletUserController.class);
    private final SavedEwalletUserService savedEwalletUserService;

    @GetMapping("/saved-ewallet-users")
    public ResponseEntity<BaseResponse<SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse>>> getSavedEwallets(
            @Parameter(
                    name = "ewalletName",
                    required = true,
                    schema = @Schema(type = "string", example = "Ovo")
            ) @RequestParam(required = false) String ewalletName) {
        SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse> savedEwallets = savedEwalletUserService.getSavedEwalletUsers(ewalletName);
        BaseResponse<SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse>> response = BaseResponse.success(HttpStatus.OK, savedEwallets, "Success Get Saved E-Wallets");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/favorite-ewallets/{id}")
    public ResponseEntity<FavoriteResponse> putFavoriteEwalletUser(
            @PathVariable UUID id,
            @RequestParam boolean isFavorite) {
        FavoriteResponse response = savedEwalletUserService.putFavoriteEwalletUser(id, isFavorite);
        return ResponseEntity.ok(response);
    }
}
