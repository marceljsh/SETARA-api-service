package org.synrgy.setara.contact.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.synrgy.setara.contact.dto.FavoriteEwalletRequest;
import org.synrgy.setara.contact.dto.FavoriteResponse;
import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.dto.SavedEwalletUserResponse;
import org.synrgy.setara.contact.exception.SavedEwalletExceptions;
import org.synrgy.setara.contact.model.SavedEwalletUser;
import org.synrgy.setara.contact.repository.SavedEwalletUserRepository;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.user.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SavedEwalletUserServiceImpl implements SavedEwalletUserService {
    private final Logger log = LoggerFactory.getLogger(SavedEwalletUserServiceImpl.class);
    private final SavedEwalletUserRepository savedEwalletUserRepo;
    private final UserRepository userRepo;
    private final EwalletUserRepository ewalletUserRepo;

    @Override
    @Transactional
    public SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse> getSavedEwalletUsers(User user, String ewalletName) {
        List<SavedEwalletUser> savedEwalletUsers = savedEwalletUserRepo.findByOwnerIdAndEwalletName(user.getId(), ewalletName);

        List<SavedEwalletUserResponse> favoriteEwalletUsers = savedEwalletUsers.stream()
                .filter(SavedEwalletUser::isFavorite)
                .map(SavedEwalletUserResponse::from)
                .toList();

        List<SavedEwalletUserResponse> nonFavoriteEwalletUsers = savedEwalletUsers.stream()
                .filter(savedUser -> !savedUser.isFavorite())
                .map(SavedEwalletUserResponse::from)
                .toList();

        long favoriteCount = favoriteEwalletUsers.size();
        long nonFavoriteCount = nonFavoriteEwalletUsers.size();

        return new SavedEwalletAndAccountFinalResponse<>(
                favoriteCount,
                nonFavoriteCount,
                favoriteEwalletUsers,
                nonFavoriteEwalletUsers
        );
    }

    @Override
    @Transactional
    public FavoriteResponse putFavoriteEwalletUser(FavoriteEwalletRequest request) {
        SavedEwalletUser savedEwalletUser = savedEwalletUserRepo.findById(request.getIdTersimpan())
                .orElseThrow(() -> new SavedEwalletExceptions.EwalletUserNotFoundException("Saved ewallet user with ID " + request.getIdTersimpan() + " not found"));

        savedEwalletUser.setFavorite(request.isFavorite());
        savedEwalletUserRepo.save(savedEwalletUser);
        return new FavoriteResponse(request.getIdTersimpan(), request.isFavorite());
    }
}
