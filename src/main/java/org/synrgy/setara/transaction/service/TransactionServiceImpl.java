package org.synrgy.setara.transaction.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.app.util.Constants;
import org.synrgy.setara.contact.model.BankContact;
import org.synrgy.setara.contact.model.EwalletContact;
import org.synrgy.setara.contact.repository.BankContactRepository;
import org.synrgy.setara.contact.repository.EwalletContactRepository;
import org.synrgy.setara.transaction.dto.MutationRequest;
import org.synrgy.setara.transaction.dto.MutationResponse;
import org.synrgy.setara.transaction.dto.MonthlyReportResponse;
import org.synrgy.setara.transaction.dto.QRPaymentRequest;
import org.synrgy.setara.transaction.dto.QRPaymentResponse;
import org.synrgy.setara.transaction.dto.TopUpRequest;
import org.synrgy.setara.transaction.dto.TopUpResponse;
import org.synrgy.setara.transaction.dto.TransferRequest;
import org.synrgy.setara.transaction.dto.TransferResponse;
import org.synrgy.setara.transaction.exception.*;
import org.synrgy.setara.transaction.model.MutationType;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.model.TransactionType;
import org.synrgy.setara.transaction.repository.TransactionRepository;
import org.synrgy.setara.transaction.util.TxUtils;
import org.synrgy.setara.user.exception.EwalletUserNotFoundException;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.exception.BankNotFoundException;
import org.synrgy.setara.vendor.exception.MerchantNotFoundException;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.model.Merchant;
import org.synrgy.setara.vendor.repository.BankRepository;
import org.synrgy.setara.vendor.repository.MerchantRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

  private static final String DEPOSIT_PREFIX = "DPT";
  private static final String TOP_UP_PREFIX = "TOP";
  private static final String TRANSFER_PREFIX = "TRF";
  private static final String TRADE_PREFIX = "QRP";
  private static final BigDecimal ADMIN_FEE = BigDecimal.valueOf(1_000);
  private static final BigDecimal MINIMUM_TOP_UP_AMOUNT = BigDecimal.valueOf(10_000);
  private static final BigDecimal MINIMUM_TRANSFER_AMOUNT = BigDecimal.valueOf(10_000);

  private final PasswordEncoder passwordEncoder;
  private final BankRepository bankRepo;
  private final BankContactRepository bcRepo;
  private final EwalletContactRepository ecRepo;
  private final EwalletUserRepository ewalletUserRepo;
  private final MerchantRepository merchantRepo;
  private final TransactionRepository txRepo;
  private final UserRepository userRepo;

  @Override
  @Transactional
  public TopUpResponse topUp(User owner, TopUpRequest request) {
    validateMpin(request.getMpin(), owner.getMpin());
    if (request.getAmount().compareTo(MINIMUM_TOP_UP_AMOUNT) < 0) {
      log.error(Constants.ERR_MINIMUM_AMOUNT + " (Top-up)");
      throw new InvalidTransactionAmountException(Constants.ERR_MINIMUM_AMOUNT);
    }

    BigDecimal totalAmount = request.getAmount().add(ADMIN_FEE);
    validateBalance(owner.getBalance(), totalAmount);

    EwalletUser target = getEwalletUser(request.getEwalletId(), request.getPhoneNumber());
    Transaction transaction = createTransaction(owner, target, null, null, TransactionType.TOP_UP,
      request.getAmount(), ADMIN_FEE, request.getPhoneNumber(), request.getNote(), TOP_UP_PREFIX);

    userRepo.updateBalanceById(owner.getId(), owner.getBalance().subtract(totalAmount));
    txRepo.save(transaction);

    if (request.isSaveContact()) {
      EwalletContact newContact = EwalletContact.builder()
        .owner(owner)
        .ewalletUser(target)
        .name(request.getName())
        .build();
      ecRepo.save(newContact);
    }

    return TopUpResponse.of(transaction, target.getName());
  }

  @Override
  @Transactional
  public TransferResponse transfer(User owner, TransferRequest request) {
    validateMpin(request.getMpin(), owner.getMpin());
    if (request.getAmount().compareTo(MINIMUM_TRANSFER_AMOUNT) < 0) {
      log.error(Constants.ERR_MINIMUM_AMOUNT + " (Transfer)");
      throw new InvalidTransactionAmountException(Constants.ERR_MINIMUM_AMOUNT);
    }
    if (request.getDestAccountNumber().equals(owner.getAccountNumber())) {
      log.error(Constants.ERR_SAME_ACCOUNT_TRANSFER);
      throw new InvalidTransactionDestinationException("Cannot transfer to own account");
    }

    Bank destBank = getBankById(request.getDestBankId());
    boolean internalTransfer = "Tahapan BCA".equals(destBank.getName());
    BigDecimal totalAmount = internalTransfer ? request.getAmount() : request.getAmount().add(ADMIN_FEE);
    validateBalance(owner.getBalance(), totalAmount);

    Transaction transferTx = createTransaction(owner, null, destBank, null, TransactionType.TRANSFER,
      request.getAmount(), internalTransfer ? BigDecimal.ZERO : ADMIN_FEE,
      request.getDestAccountNumber(), request.getNote(), TRANSFER_PREFIX);

    txRepo.save(transferTx);
    userRepo.updateBalanceById(owner.getId(), owner.getBalance().subtract(totalAmount));

    if (internalTransfer) {
      Transaction depositTx = createTransaction(owner, null, destBank, null, TransactionType.DEPOSIT,
        request.getAmount(), BigDecimal.ZERO, null, request.getNote(), DEPOSIT_PREFIX);
      txRepo.save(depositTx);
      userRepo.updateBalanceByAccountNumber(request.getDestAccountNumber(), request.getAmount());
    }

    if (request.isSaveContact()) {
      BankContact contact = BankContact.builder()
          .owner(owner)
          .bank(destBank)
          .name(request.getName())
          .accountNumber(request.getDestAccountNumber())
          .favorite(false)
          .build();
      bcRepo.save(contact);
    }

    return TransferResponse.from(transferTx);
  }

  @Override
  @Transactional
  public QRPaymentResponse payWithQRIS(User owner, QRPaymentRequest request) {
    validateMpin(request.getMpin(), owner.getMpin());
    if (request.getAmount().compareTo(BigDecimal.ONE) < 0) {
      log.error(Constants.ERR_MINIMUM_AMOUNT + " (QR Payment)");
      throw new InvalidTransactionAmountException(Constants.ERR_MINIMUM_AMOUNT);
    }

    validateBalance(owner.getBalance(), request.getAmount());
    Merchant merchant = getMerchantById(request.getMerchantId());

    Transaction transaction = createTransaction(owner, null, null, merchant, TransactionType.QR_PAYMENT,
      request.getAmount(), BigDecimal.ZERO, null, request.getNote(), TRADE_PREFIX);

    txRepo.save(transaction);
    userRepo.updateBalanceById(owner.getId(), owner.getBalance().subtract(request.getAmount()));

    return QRPaymentResponse.from(transaction);
  }

  @Override
  @Transactional(readOnly = true)
  public MonthlyReportResponse generateMonthlyReport(User owner, int year, int month) {
    validateYearAndMonth(year, month);

    List<Transaction> expenseTransactions = txRepo.findMonthlyReportMultipleTypes(owner.getId(),
      List.of(TransactionType.TRANSFER, TransactionType.TOP_UP), month, year);

    List<Transaction> incomeTransactions = txRepo.findMonthlyReportSingleTypes(owner.getId(),
      TransactionType.DEPOSIT, month, year);

    BigDecimal income = incomeTransactions.stream()
      .map(Transaction::getAmount)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal expense = expenseTransactions.stream()
      .map(Transaction::getAmount)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    return MonthlyReportResponse.of(month, year, income, expense);
  }

  @Override
  @Transactional
  public Page<MutationResponse> getMutationList(User owner, MutationRequest request) {
    Specification<Transaction> spec = ((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>(List.of());
      if (Objects.nonNull(request.getStartDate())) {
        predicates.add(cb.greaterThanOrEqualTo(
            root.get("transactionTime"), request.getStartDate()));
      }

      if (Objects.nonNull(request.getEndDate())) {
        predicates.add(cb.lessThanOrEqualTo(
            root.get("transactionTime"), request.getEndDate()));
      }

      if (request.getType() == MutationType.CREDIT) {
        predicates.add(cb.equal(root.get("type"), TransactionType.DEPOSIT));
      } else if (request.getType() == MutationType.DEBIT) {
        predicates.add(cb.notEqual(root.get("type"), TransactionType.DEPOSIT));
      }

      return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
    });

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Transaction> transactions = txRepo.findAll(spec, pageable);

    log.info("Found {} transaction log", transactions.getTotalElements());

    List<MutationResponse> result = transactions
        .map(MutationResponse::from)
        .getContent();

    return new PageImpl<>(result, pageable, transactions.getTotalElements());
  }

  @Override
  @Transactional
  public MutationResponse getMutationDetail(UUID txId) {
    Transaction transaction = txRepo.findById(txId).orElseThrow(() -> {
      log.error("No such Transaction with id={}", txId);
      return new TransactionNotFoundException(Constants.ERR_TRANSACTION_NOT_FOUND);
    });

    return MutationResponse.from(transaction);
  }

  private void validateMpin(String rawMpin, String encodedMpin) {
    if (!passwordEncoder.matches(rawMpin, encodedMpin)) {
      log.error("Invalid MPIN");
      throw new InvalidMpinException(Constants.ERR_INCORRECT_CREDENTIAL);
    }
  }

  private void validateBalance(BigDecimal balance, BigDecimal totalAmount) {
    if (balance.compareTo(totalAmount) < 0) {
      log.error("Balance ({}) is less than total amount ({})", balance, totalAmount);
      throw new InsufficientBalanceException(Constants.ERR_INSUFFICIENT_BALANCE);
    }
  }

  private EwalletUser getEwalletUser(UUID ewalletId, String phoneNumber) {
    return ewalletUserRepo.findByPhoneNumberAndEwalletId(phoneNumber, ewalletId)
        .orElseThrow(() -> {
          log.error("No such E-Wallet User with ewallet_id={} and phone={}", ewalletId, phoneNumber);
          return new EwalletUserNotFoundException(Constants.ERR_EWALLET_USER_NOT_FOUND);
        });
  }

  private Bank getBankById(UUID bankId) {
    return bankRepo.findById(bankId)
        .orElseThrow(() -> {
          log.error("No such Bank with id={}", bankId);
          return new BankNotFoundException(Constants.ERR_BANK_NOT_FOUND);
        });
  }

  private Merchant getMerchantById(UUID merchantId) {
    return merchantRepo.findById(merchantId)
        .orElseThrow(() -> {
          log.error("No such Merchant with id={}", merchantId);
          return new MerchantNotFoundException(Constants.ERR_MERCHANT_NOT_FOUND);
        });
  }

  private String generateUniqueReferenceNumber(String prefix) {
    int attempts = 0;
    String referenceNumber;
    do {
      if (attempts == Constants.MAX_GENERATION_ATTEMPTS) {
        log.error(Constants.ERR_REFERENCE_NUMBER_GENERATION);
        throw new ReferenceNumberGenerationException(Constants.ERR_REFERENCE_NUMBER_GENERATION);
      }
      referenceNumber = TxUtils.generateReferenceNumber(prefix);
      attempts++;
    } while (txRepo.existsByReferenceNumber(referenceNumber));
    return referenceNumber;
  }

  private Transaction createTransaction(User user, EwalletUser ewalletUser, Bank bank, Merchant merchant,
      TransactionType type, BigDecimal amount, BigDecimal adminFee,
      String destination, String note, String prefix) {
    String referenceNumber = generateUniqueReferenceNumber(prefix);
    String uniqueCode = TxUtils.generateUniqueCode(referenceNumber);
    LocalDateTime now = LocalDateTime.now();

    return Transaction.builder()
        .user(user)
        .ewallet(ewalletUser != null ? ewalletUser.getEwallet() : null)
        .bank(bank)
        .merchant(merchant)
        .type(type)
        .amount(amount)
        .adminFee(adminFee)
        .destPhoneNumber(type == TransactionType.TOP_UP ? destination : null)
        .destAccountNumber(type == TransactionType.TRANSFER ? destination : null)
        .referenceNumber(referenceNumber)
        .uniqueCode(uniqueCode)
        .note(note)
        .transactionTime(now)
        .build();
  }

  private void validateYearAndMonth(int year, int month) {
    if (month < 1 || month > 12 || year < 1900 || year > LocalDateTime.now().getYear()) {
      log.error(Constants.ERR_INVALID_TIMESPAN);
      throw new InvalidTimespanException(Constants.ERR_INVALID_TIMESPAN);
    }
  }

}
