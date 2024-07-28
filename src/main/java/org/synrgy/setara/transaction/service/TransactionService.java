package org.synrgy.setara.transaction.service;

import org.synrgy.setara.transaction.dto.*;

public interface TransactionService {
    TopUpResponse topUp(TopUpRequest request);

    TransferResponse transferWithinBCA(TransferRequest request);

    MonthlyReportResponse getMonthlyReport(int month, int year);

    MerchantTransactionResponse merchantTransaction(MerchantTransactionRequest request);
}
