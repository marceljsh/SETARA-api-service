package org.synrgy.setara.transaction.service;

import org.synrgy.setara.transaction.dto.*;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TopUpResponse topUp(User user, TopUpRequest request);

    TransferResponse transferWithinBCA(User user, TransferRequest request);

    MonthlyReportResponse getMonthlyReport(User user, int month, int year);

    MerchantTransactionResponse merchantTransaction(User user, MerchantTransactionRequest request);

    MutationResponseWithPagination getAllMutation(User user, MutationRequest request, int page, int size);

    MutationDetailResponse getMutationDetail(User user, UUID transactionId);

    List<MutationDatasetResponse> getMutationDataset(User user);

    Transaction getByTransactionId(UUID transactionId);
}
