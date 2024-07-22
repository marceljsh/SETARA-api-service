package org.synrgy.setara.transaction.service;

import org.synrgy.setara.transaction.dto.TransactionRequest;
import org.synrgy.setara.transaction.dto.TransactionResponse;

public interface TransactionService {
    TransactionResponse topUp(TransactionRequest request, String token);
}
