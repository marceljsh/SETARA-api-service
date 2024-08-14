package org.synrgy.setara.transaction.service;

import org.springframework.data.domain.Page;
import org.synrgy.setara.transaction.dto.MonthlyReportResponse;
import org.synrgy.setara.transaction.dto.MutationRequest;
import org.synrgy.setara.transaction.dto.MutationResponse;
import org.synrgy.setara.transaction.dto.QRPaymentRequest;
import org.synrgy.setara.transaction.dto.QRPaymentResponse;
import org.synrgy.setara.transaction.dto.TopUpRequest;
import org.synrgy.setara.transaction.dto.TopUpResponse;
import org.synrgy.setara.transaction.dto.TransferRequest;
import org.synrgy.setara.transaction.dto.TransferResponse;
import org.synrgy.setara.user.model.User;

import java.util.UUID;

public interface TransactionService {

  TopUpResponse topUp(User owner, TopUpRequest request);

  TransferResponse transfer(User owner, TransferRequest request);

  QRPaymentResponse payWithQRIS(User owner, QRPaymentRequest request);

  MonthlyReportResponse generateMonthlyReport(User owner, int year, int month);

  Page<MutationResponse> getMutationList(User owner, MutationRequest request);

  MutationResponse getMutationDetail(UUID txId);

}
