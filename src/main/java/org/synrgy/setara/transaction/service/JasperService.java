package org.synrgy.setara.transaction.service;

import org.synrgy.setara.transaction.dto.TransferResponse;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.user.model.User;

import java.util.UUID;

public interface JasperService {
    boolean generateReceipt(User user, UUID transactionId);

    boolean generateAllMutationReport(User user);
}

