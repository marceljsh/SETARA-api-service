package org.synrgy.setara.transaction.service;

import org.synrgy.setara.transaction.dto.TransferResponse;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.user.model.User;

public interface JasperService {
    byte[] generateReceipt(Transaction transaction, TransferResponse response);

    boolean generateAllMutationReport(User user);
}

