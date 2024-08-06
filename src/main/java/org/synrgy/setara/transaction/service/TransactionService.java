package org.synrgy.setara.transaction.service;

import org.synrgy.setara.transaction.dto.*;
import org.synrgy.setara.user.model.User;

public interface TransactionService {

  TopUpResponse topUp(User user, TopUpRequest request);

  TransferResponse transfer(User user, TransferRequest request);

  MonthlyReportResponse getMonthlyReport(User user, int month, int year);

//  MerchantTransactionResponse merchantTransaction(MerchantTransactionRequest request);

}
