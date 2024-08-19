package org.synrgy.setara.contact.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.synrgy.setara.contact.dto.FavoriteTransactionsResponse;
import org.synrgy.setara.contact.model.SavedAccount;
import org.synrgy.setara.contact.model.SavedEwalletUser;
import org.synrgy.setara.contact.repository.SavedAccountRepository;
import org.synrgy.setara.contact.repository.SavedEwalletUserRepository;
import org.synrgy.setara.transaction.model.TransactionType;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.model.Ewallet;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class FavoriteTransactionServiceTest {
    @InjectMocks
    FavoriteTransactionServiceImpl favoriteTransactionService;

    @Mock
    SavedAccountRepository savedAccountRepository;

    @Mock
    SavedEwalletUserRepository savedEwalletUserRepository;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFavoriteTransactions_Success() {
        User user = new User();
        user.setId(UUID.randomUUID());

        Bank bank = Bank.builder().name("BCA").build();

        SavedAccount favoriteAccount1 = SavedAccount.builder()
                .accountNumber("123456789")
                .bank(bank)
                .favorite(true)
                .transferCount(10)
                .build();
        SavedAccount favoriteAccount2 = SavedAccount.builder()
                .accountNumber("987654321")
                .bank(bank)
                .favorite(true)
                .transferCount(8)
                .build();
        SavedAccount favoriteAccount3 = SavedAccount.builder()
                .accountNumber("1122334455")
                .bank(bank)
                .favorite(true)
                .transferCount(8)
                .build();

        User destinationUser1 = User.builder().name("John Doe").build();
        User destinationUser2 = User.builder().name("Jane Smith").build();
        User destinationUser3 = User.builder().name("Mary Sue").build();

        List<SavedAccount> favoriteAccounts = List.of(favoriteAccount1, favoriteAccount2, favoriteAccount3);

        when(userRepository.findByAccountNumber(favoriteAccount1.getAccountNumber())).thenReturn(Optional.of(destinationUser1));
        when(userRepository.findByAccountNumber(favoriteAccount2.getAccountNumber())).thenReturn(Optional.of(destinationUser2));
        when(userRepository.findByAccountNumber(favoriteAccount3.getAccountNumber())).thenReturn(Optional.of(destinationUser3));
        when(savedAccountRepository.findTop5ByOwnerIdAndFavoriteTrueOrderByTransferCountDesc(user.getId())).thenReturn(favoriteAccounts);

        Ewallet ewallet = new Ewallet();
        ewallet.setName("OVO");

        EwalletUser ewalletUser1 = EwalletUser.builder()
                .name("Lily")
                .ewallet(ewallet)
                .build();
        EwalletUser ewalletUser2 = EwalletUser.builder()
                .name("Tulip")
                .ewallet(ewallet)
                .build();
        EwalletUser ewalletUser3 = EwalletUser.builder()
                .name("Rose")
                .ewallet(ewallet)
                .build();

        SavedEwalletUser favoriteEwalletUser1 = SavedEwalletUser.builder()
                .ewalletUser(ewalletUser1)
                .favorite(true)
                .transferCount(5)
                .build();
        SavedEwalletUser favoriteEwalletUser2 = SavedEwalletUser.builder()
                .ewalletUser(ewalletUser2)
                .favorite(true)
                .transferCount(11)
                .build();
        SavedEwalletUser favoriteEwalletUser3 = SavedEwalletUser.builder()
                .ewalletUser(ewalletUser3)
                .favorite(true)
                .transferCount(7)
                .build();

        List<SavedEwalletUser> favoriteEwalletUsers = List.of(favoriteEwalletUser1, favoriteEwalletUser2, favoriteEwalletUser3);

        when(savedEwalletUserRepository.findTop5ByOwnerIdAndFavoriteTrueOrderByTransferCountDesc(user.getId())).thenReturn(favoriteEwalletUsers);

        List<FavoriteTransactionsResponse> expectedResponse = List.of(
                FavoriteTransactionsResponse.builder()
                        .type(TransactionType.TOP_UP)
                        .bankOrEwalletName("OVO")
                        .name("Tulip")
                        .build(),
                FavoriteTransactionsResponse.builder()
                        .type(TransactionType.TRANSFER)
                        .bankOrEwalletName("BCA")
                        .name("John Doe")
                        .build(),
                FavoriteTransactionsResponse.builder()
                        .type(TransactionType.TRANSFER)
                        .bankOrEwalletName("BCA")
                        .name("Jane Smith")
                        .build(),
                FavoriteTransactionsResponse.builder()
                        .type(TransactionType.TRANSFER)
                        .bankOrEwalletName("BCA")
                        .name("Mary Sue")
                        .build(),
                FavoriteTransactionsResponse.builder()
                        .type(TransactionType.TOP_UP)
                        .bankOrEwalletName("OVO")
                        .name("Rose")
                        .build()
        );

        List<FavoriteTransactionsResponse> response = favoriteTransactionService.getFavoriteTransactions(user);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }
}
