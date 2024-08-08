package org.synrgy.setara.contact.service;

import org.synrgy.setara.contact.dto.FavoriteTransactionsResponse;
import org.synrgy.setara.user.model.User;

import java.util.List;

public interface FavoriteTransactionService {
    List<FavoriteTransactionsResponse> getFavoriteTransactions(User user);
}
