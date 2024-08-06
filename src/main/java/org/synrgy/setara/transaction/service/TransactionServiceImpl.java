package org.synrgy.setara.transaction.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.app.util.Constants;
import org.synrgy.setara.contact.model.EwalletContact;
import org.synrgy.setara.contact.repository.EwalletContactRepository;
import org.synrgy.setara.transaction.dto.*;
import org.synrgy.setara.transaction.exception.*;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.model.TransactionType;
import org.synrgy.setara.transaction.repository.TransactionRepository;
import org.synrgy.setara.transaction.utils.TransactionUtils;
import org.synrgy.setara.user.exception.EwalletUserNotFoundException;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.exception.BankNotFoundException;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.repository.BankRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

  private static final String DEPOSIT_PREFIX = "DPT";

  private static final String TOP_UP_PREFIX = "TOP";

  private static final String TRANSFER_PREFIX = "TRF";

  private static final BigDecimal ADMIN_FEE = BigDecimal.valueOf(1000);

  private static final BigDecimal MINIMUM_TOP_UP_AMOUNT = BigDecimal.valueOf(10000);

  private static final BigDecimal MINIMUM_TRANSFER_AMOUNT = BigDecimal.valueOf(10000);

  private final PasswordEncoder passwordEncoder;

  private final TransactionRepository txRepo;

  private final EwalletUserRepository euRepo;

  private final UserRepository userRepo;

  private final EwalletContactRepository ecRepo;

  private final BankRepository bankRepo;

  private void validateMpin(String rawMpin, String encodedMpin) {
    if (!passwordEncoder.matches(rawMpin, encodedMpin)) {
      log.error("Invalid MPIN");
      throw new InvalidMpinException(Constants.ERR_INCORRECT_CREDENTIAL);
    }
  }

  private void checkSufficientBalance(BigDecimal balance, BigDecimal totalAmount) {
    if (balance.compareTo(totalAmount) < 0) {
      log.error("Balance ({}) is less than total amount ({})", balance, totalAmount);
      throw new InsufficientBalanceException(Constants.ERR_INSUFFICIENT_BALANCE);
    }
  }

  private EwalletUser findEwalletUser(UUID ewalletId, String phoneNumber) {
    return euRepo.findByPhoneNumberAndEwalletId(phoneNumber, ewalletId).orElseThrow(() -> {
      log.error("No such E-Wallet User with ewallet_id={} and phone={}", ewalletId, phoneNumber);
      return new EwalletUserNotFoundException(Constants.ERR_EWALLET_USER_NOT_FOUND);
    });
  }

  private Bank findBankById(UUID bankId) {
    return bankRepo.findById(bankId).orElseThrow(() -> {
      log.error("No such Bank with id={}", bankId);
      return new BankNotFoundException(Constants.ERR_BANK_NOT_FOUND);
    });
  }

  private String generateReferenceNumber(String prefix) {
    String referenceNumber;
    int attempts = 0;

    do {
      if (attempts == Constants.MAX_GENERATION_ATTEMPTS) {
        log.error(Constants.ERR_REFERENCE_NUMBER_GENERATION);
        throw new ReferenceNumberGenerationException(Constants.ERR_REFERENCE_NUMBER_GENERATION);
      }

      referenceNumber = TransactionUtils.generateReferenceNumber(prefix);
      attempts++;

    } while (txRepo.existsByReferenceNumber(referenceNumber));

    return referenceNumber;
  }

  private Transaction createTopUpTx(User user, EwalletUser target, BigDecimal nominal, String note) {
    String refNo = generateReferenceNumber(TOP_UP_PREFIX);
    String uniqueCode = TransactionUtils.generateUniqueCode(refNo);

    return Transaction.builder()
        .user(user)
        .ewallet(target.getEwallet())
        .type(TransactionType.TOP_UP)
        .destPhoneNumber(target.getPhoneNumber())
        .nominal(nominal)
        .adminFee(ADMIN_FEE)
        .uniqueCode(uniqueCode)
        .referenceNumber(refNo)
        .note(note)
        .transactionTime(LocalDateTime.now())
        .build();
  }

  private Transaction createTransferTx(User user, Bank bank, String destAccNumber, BigDecimal nominal, String note) {
     String refNo = generateReferenceNumber(TRANSFER_PREFIX);
     String uniqueCode = TransactionUtils.generateUniqueCode(refNo);

    return Transaction.builder()
        .user(user)
        .bank(bank)
        .type(TransactionType.TRANSFER)
        .destAccountNumber(destAccNumber)
        .nominal(nominal)
        .adminFee(bank.getName().equals("Tahapan BCA") ? BigDecimal.ZERO : ADMIN_FEE)
        .uniqueCode(uniqueCode)
        .referenceNumber(refNo)
        .note(note)
        .build();
  }

  private Transaction createDepositTx(User user, Bank bank, BigDecimal nominal, BigDecimal adminFee, String note) {
    String refNo = generateReferenceNumber(DEPOSIT_PREFIX);
    String uniqueCode = TransactionUtils.generateUniqueCode(refNo);

    return Transaction.builder()
        .user(user)
        .bank(bank)
        .type(TransactionType.DEPOSIT)
        .nominal(nominal)
        .adminFee(adminFee)
        .uniqueCode(uniqueCode)
        .referenceNumber(refNo)
        .note(note)
        .transactionTime(LocalDateTime.now())
        .build();
  }

  private void validateMonthAndYear(int month, int year) {
    if (month < 1 || month > 12) {
      log.error(Constants.ERR_INVALID_TIMESPAN);
      throw new InvalidTimespanException(Constants.ERR_INVALID_TIMESPAN);
    }

    int currentYear = LocalDateTime.now().getYear();
    if (year < 1900 || year > currentYear) {
      log.error(Constants.ERR_INVALID_TIMESPAN);
      throw new InvalidTimespanException(Constants.ERR_INVALID_TIMESPAN);
    }
  }

  @Override
  @Transactional
  public TopUpResponse topUp(User user, TopUpRequest request) {
    validateMpin(request.getMpin(), user.getMpin());

    if (request.getAmount().compareTo(MINIMUM_TOP_UP_AMOUNT) < 0) {
      log.error(Constants.ERR_MINIMUM_TOP_UP);
      throw new InvalidTransactionAmountException(Constants.ERR_MINIMUM_TOP_UP);
    }

    BigDecimal totalAmount = request.getAmount().add(ADMIN_FEE);
    checkSufficientBalance(user.getBalance(), totalAmount);

    EwalletUser target = findEwalletUser(request.getEwalletId(), request.getPhoneNumber());
    Transaction tx = createTopUpTx(user, target, request.getAmount(), request.getNote());

    BigDecimal newBalance = user.getBalance().subtract(totalAmount);
    userRepo.updateBalanceById(user.getId(), newBalance);

    txRepo.save(tx);

    if (request.isSaveContact()) {
      EwalletContact newContact = EwalletContact.builder()
          .owner(user)
          .ewalletUser(target)
          .name(request.getName())
          .build();
      ecRepo.save(newContact);
    }

    return TopUpResponse.of(tx, target.getName());
  }

  @Override
  @Transactional
  public TransferResponse transfer(User user, TransferRequest request) {
    validateMpin(request.getMpin(), user.getMpin());

    if (request.getAmount().compareTo(MINIMUM_TRANSFER_AMOUNT) < 0) {
      log.error(Constants.ERR_MINIMUM_TRANSFER);
      throw new InvalidTransactionAmountException(Constants.ERR_MINIMUM_TRANSFER);
    }

    if (request.getDestAccountNumber().equals(user.getAccountNumber())) {
      log.error(Constants.ERR_SAME_ACCOUNT_TRANSFER);
      throw new InvalidTransactionDestinationException("Cannot transfer to own account");
    }

    Bank destBank = findBankById(request.getDestBankId());

    BigDecimal totalAmount = request.getAmount();
    boolean internalTransfer = destBank.getName().equals("Tahapan BCA");
    if (!internalTransfer) {
      totalAmount = totalAmount.add(ADMIN_FEE);
    }

    checkSufficientBalance(user.getBalance(), request.getAmount());

    Transaction transferTx = createTransferTx(user, destBank, request.getDestAccountNumber(), request.getAmount(), request.getNote());
    Transaction depositTx = createDepositTx(user, destBank, request.getAmount(), internalTransfer ? BigDecimal.ZERO : ADMIN_FEE, request.getNote());

    txRepo.save(transferTx);
    txRepo.save(depositTx);

    BigDecimal newBalance = user.getBalance().subtract(totalAmount);
    userRepo.updateBalanceById(user.getId(), newBalance);

    if (internalTransfer) {
      userRepo.updateBalanceByAccountNumber(request.getDestAccountNumber(), request.getAmount());
    }

    return TransferResponse.from(transferTx);
  }

  @Override
  @Transactional(readOnly = true)
  public MonthlyReportResponse getMonthlyReport(User user, int month, int year) {
    validateMonthAndYear(month, year);

    List<Transaction> expenseTxs = txRepo.findMonthlyReportMultipleTypes(user.getId(),
        List.of(TransactionType.TRANSFER, TransactionType.TOP_UP), month, year);

    List<Transaction> incomeTxs = txRepo.findMonthlyReportSingleTypes(user.getId(),
        TransactionType.DEPOSIT, month, year);

    BigDecimal income = incomeTxs.stream()
        .map(Transaction::getNominal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal expense = expenseTxs.stream()
        .map(Transaction::getNominal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return MonthlyReportResponse.of(month, year, income, expense);
  }

}
