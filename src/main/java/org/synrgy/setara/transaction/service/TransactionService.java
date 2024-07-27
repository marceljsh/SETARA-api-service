package org.synrgy.setara.transaction.service;

import org.synrgy.setara.transaction.dto.*;

public interface TransactionService {
    TopUpResponse topUp(TopUpRequest request, String token);

    TransferResponse transferWithinBCA(TransferRequest request, String authToken);

    MonthlyReportResponse getMonthlyReport(String token, int month, int year);

    MerchantTransactionResponse merchantTransaction(MerchantTransactionRequest request, String authToken);
}
