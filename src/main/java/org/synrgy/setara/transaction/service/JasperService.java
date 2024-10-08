package org.synrgy.setara.transaction.service;

import org.synrgy.setara.user.model.User;

import java.util.UUID;

public interface JasperService {
    byte[] generateReceipt(User user, UUID transactionId);

    byte[] generateAllMutationReport(User user);
}

