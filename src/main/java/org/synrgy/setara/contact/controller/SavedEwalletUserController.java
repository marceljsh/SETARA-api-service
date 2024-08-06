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
import org.synrgy.setara.contact.dto.*;
import org.synrgy.setara.contact.service.SavedEwalletUserService;
import org.synrgy.setara.user.model.User;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SavedEwalletUserController {
    private final Logger log = LoggerFactory.getLogger(SavedEwalletUserController.class);
    private final SavedEwalletUserService savedEwalletUserService;

    @GetMapping("/saved-ewallet-users")
    public ResponseEntity<BaseResponse<SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse>>> getSavedEwallets(
      User user,
            @Parameter(
                    name = "ewalletName",
                    required = true,
                    schema = @Schema(type = "string", allowableValues = {"Ovo", "ShopeePay", "GoPay", "DANA", "LinkAja"}, example = "Ovo")
            ) @RequestParam(required = false) String ewalletName) {
        SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse> savedEwallets = savedEwalletUserService.getSavedEwalletUsers(user, ewalletName);
        BaseResponse<SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse>> response = BaseResponse.success(HttpStatus.OK, savedEwallets, "Success Get Saved E-Wallets");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/favorite-ewallet")
    public ResponseEntity<BaseResponse<FavoriteResponse>> putFavoriteEwalletUser(@RequestBody FavoriteEwalletRequest request) {
        FavoriteResponse favoriteResponse = savedEwalletUserService.putFavoriteEwalletUser(request);
        BaseResponse<FavoriteResponse> response = BaseResponse.success(HttpStatus.OK, favoriteResponse, "Success update is favorite E-Wallet");
        return ResponseEntity.ok(response);
    }
}
