package org.synrgy.setara.transaction.service;

import org.synrgy.setara.transaction.dto.TransferRequestDTO;
import org.synrgy.setara.transaction.dto.TransactionRequest;
import org.synrgy.setara.transaction.dto.TransactionResponse;
import org.synrgy.setara.transaction.dto.TransferResponseDTO;

public interface TransactionService {
    TransactionResponse topUp(TransactionRequest request, String token);

    TransferResponseDTO transferWithinBCA(TransferRequestDTO request, String authToken);
}
