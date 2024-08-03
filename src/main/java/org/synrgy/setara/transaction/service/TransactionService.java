package org.synrgy.setara.transaction.service;

import org.springframework.data.domain.Page;
import org.synrgy.setara.transaction.dto.*;

import java.util.UUID;

public interface TransactionService {
    TopUpResponse topUp(TopUpRequest request);

    TransferResponse transferWithinBCA(TransferRequest request);

    MonthlyReportResponse getMonthlyReport(int month, int year);

    MerchantTransactionResponse merchantTransaction(MerchantTransactionRequest request);

    Page<MutationResponse> getAllMutation(MutationRequest request, int page, int size);

    MutationDetailResponse getMutationDetail(UUID transactionId);
}
