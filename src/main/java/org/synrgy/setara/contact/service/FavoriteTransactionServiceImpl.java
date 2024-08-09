package org.synrgy.setara.contact.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.synrgy.setara.contact.dto.FavoriteTransactionsResponse;
import org.synrgy.setara.contact.exception.SavedAccountExceptions;
import org.synrgy.setara.contact.model.SavedAccount;
import org.synrgy.setara.contact.model.SavedEwalletUser;
import org.synrgy.setara.contact.repository.SavedAccountRepository;
import org.synrgy.setara.contact.repository.SavedEwalletUserRepository;
import org.synrgy.setara.transaction.model.TransactionType;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteTransactionServiceImpl implements FavoriteTransactionService {

    private final SavedAccountRepository savedAccountRepository;

    private final SavedEwalletUserRepository savedEwalletUserRepository;

    private final UserRepository userRepository;

    @Override
    public List<FavoriteTransactionsResponse> getFavoriteTransactions(User user) {
        List<SavedAccount> favoriteAccounts = savedAccountRepository.findTop5ByOwnerIdAndFavoriteTrueOrderByTransferCountDesc(user.getId());
        List<SavedEwalletUser> favoriteEwalletUsers = savedEwalletUserRepository.findTop5ByOwnerIdAndFavoriteTrueOrderByTransferCountDesc(user.getId());

        @AllArgsConstructor
        @Getter
        class FavoriteTransaction {
            private TransactionType type;
            private String bankOrEwalletName;
            private String name;
            private int transferCount;
        }

        List<FavoriteTransaction> favoriteTransactions = new ArrayList<>();

        for (SavedAccount favoriteAccount : favoriteAccounts) {
            User destinationUser = userRepository.findByAccountNumber(favoriteAccount.getAccountNumber())
                    .orElseThrow(() -> new SavedAccountExceptions.UserNotFoundException("User with account number " + favoriteAccount.getAccountNumber() + " not found"));

            favoriteTransactions.add(new FavoriteTransaction(
                    TransactionType.TRANSFER,
                    favoriteAccount.getBank().getName(),
                    destinationUser.getName(),
                    favoriteAccount.getTransferCount()
            ));
        }

        for (SavedEwalletUser favoriteEwalletUser : favoriteEwalletUsers) {
            favoriteTransactions.add(new FavoriteTransaction(
                    TransactionType.TOP_UP,
                    favoriteEwalletUser.getEwalletUser().getEwallet().getName(),
                    favoriteEwalletUser.getEwalletUser().getName(),
                    favoriteEwalletUser.getTransferCount()
            ));
        }

        return favoriteTransactions.stream()
                .sorted((t1, t2) -> Integer.compare(t2.getTransferCount(), t1.getTransferCount()))
                .limit(5)
                .map(transaction -> FavoriteTransactionsResponse.builder()
                        .type(transaction.getType())
                        .bankOrEwalletName(transaction.getBankOrEwalletName())
                        .name(transaction.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
