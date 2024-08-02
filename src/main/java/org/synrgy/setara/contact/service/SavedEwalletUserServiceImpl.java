package org.synrgy.setara.contact.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.synrgy.setara.contact.dto.FavoriteResponse;
import org.synrgy.setara.contact.dto.SavedEwalletAndAccountFinalResponse;
import org.synrgy.setara.contact.dto.SavedEwalletUserResponse;
import org.synrgy.setara.contact.exception.SavedEwalletExceptions;
import org.synrgy.setara.contact.exception.SavedEwalletExceptions.*;
import org.synrgy.setara.contact.model.SavedEwalletUser;
import org.synrgy.setara.contact.repository.SavedEwalletUserRepository;
import org.synrgy.setara.user.model.EwalletUser;
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
    public void seedSavedEwalletUsers() {
        List<EwalletUser> ewalletUsers = ewalletUserRepo.findAll();

        if (ewalletUsers.isEmpty()) {
            log.warn("No e-wallet users found in the database.");
            return;
        }

        Optional<User> optionalOwner = userRepo.findByName("Kendrick Lamar");

        if (optionalOwner.isEmpty()) {
            log.warn("User with name 'Kendrick Lamar' not found.");
            return;
        }

        User owner = optionalOwner.get();

        for (EwalletUser ewalletUser : ewalletUsers) {
            boolean exists = savedEwalletUserRepo.existsByOwnerAndEwalletUser(owner, ewalletUser);

            if (!exists) {
                SavedEwalletUser savedEwalletUser = SavedEwalletUser.builder()
                        .owner(owner)
                        .ewalletUser(ewalletUser)
                        .favorite(false)
                        .build();

                savedEwalletUserRepo.save(savedEwalletUser);
                log.info("SavedEwalletUser with owner {} and ewalletUser {} has been added to the database", owner.getName(), ewalletUser.getName());
            } else {
                log.info("SavedEwalletUser with owner {} and ewalletUser {} already exists in the database", owner.getName(), ewalletUser.getName());
            }
        }
    }

    @Override
    @Transactional
    public SavedEwalletAndAccountFinalResponse<SavedEwalletUserResponse> getSavedEwalletUsers(String ewalletName) {
        String signature = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepo.findBySignature(signature)
                .orElseThrow(() -> new SavedEwalletExceptions.UserNotFoundException("User with signature " + signature + " not found"));

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
    public FavoriteResponse putFavoriteEwalletUser(UUID idTersimpan, boolean isFavorite) {
        Optional<SavedEwalletUser> optionalSavedEwalletUser = savedEwalletUserRepo.findById(idTersimpan);
        if (optionalSavedEwalletUser.isPresent()) {
            SavedEwalletUser savedEwalletUser = optionalSavedEwalletUser.get();
            savedEwalletUser.setFavorite(isFavorite);
            savedEwalletUserRepo.save(savedEwalletUser);
            return new FavoriteResponse(idTersimpan, isFavorite);
        } else {
            throw new EwalletUserNotFoundException("Saved e-wallet user with id " + idTersimpan + " not found");
        }
    }
}
