package org.synrgy.setara.transaction.service;

import org.springframework.data.domain.Page;
import org.synrgy.setara.transaction.dto.*;
import org.synrgy.setara.user.model.User;

import java.util.UUID;

public interface TransactionService {
    TopUpResponse topUp(User user, TopUpRequest request);

    TransferResponse transferWithinBCA(User user, TransferRequest request);

    MonthlyReportResponse getMonthlyReport(User user, int month, int year);

    MerchantTransactionResponse merchantTransaction(User user, MerchantTransactionRequest request);

    Page<MutationResponse> getAllMutation(User user, MutationRequest request, int page, int size);

    MutationDetailResponse getMutationDetail(User user, UUID transactionId);
}
