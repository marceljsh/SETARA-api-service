package org.synrgy.setara.transaction.service;

import org.synrgy.setara.transaction.dto.TransferResponse;
import org.synrgy.setara.transaction.model.Transaction;

public interface ReceiptService {
    byte[] generateReceipt(Transaction transaction, TransferResponse response);
}

