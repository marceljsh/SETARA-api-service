package org.synrgy.setara.transaction.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.synrgy.setara.contact.model.SavedAccount;
import org.synrgy.setara.contact.model.SavedEwalletUser;
import org.synrgy.setara.contact.repository.SavedAccountRepository;
import org.synrgy.setara.contact.repository.SavedEwalletUserRepository;
import org.synrgy.setara.transaction.dto.*;
import org.synrgy.setara.transaction.exception.TransactionExceptions;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.model.TransactionType;
import org.synrgy.setara.transaction.repository.TransactionRepository;
import org.synrgy.setara.transaction.utils.TransactionUtils;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.model.Merchant;
import org.synrgy.setara.vendor.repository.EwalletRepository;
import org.synrgy.setara.vendor.repository.MerchantRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EwalletRepository ewalletRepository;
    private final EwalletUserRepository ewalletUserRepository;
    private final SavedEwalletUserRepository savedEwalletUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final SavedAccountRepository savedAccountRepository;
    private final MerchantRepository merchantRepository;
    private static final BigDecimal ADMIN_FEE = BigDecimal.valueOf(1000);
    private static final BigDecimal MINIMUM_TOP_UP_AMOUNT = BigDecimal.valueOf(10000);
    private static final BigDecimal MINIMUM_TRANSFER_AMOUNT = BigDecimal.valueOf(1);

    @Override
    @Transactional
    public TopUpResponse topUp(User user, TopUpRequest request) {
        validateMpin(request.getMpin(), user);

        if (request.getAmount().compareTo(MINIMUM_TOP_UP_AMOUNT) < 0) {
            throw new TransactionExceptions.InvalidTopUpAmountException("Top-up amount must be at least " + MINIMUM_TOP_UP_AMOUNT);
        }

        BigDecimal totalAmount = request.getAmount().add(ADMIN_FEE);
        checkSufficientBalance(user, totalAmount);

        EwalletUser destinationEwalletUser = ewalletUserRepository.findByEwalletIdAndPhoneNumber(request.getIdEwallet(), request.getDestinationPhoneNumber())
                .orElseThrow(() -> new TransactionExceptions.DestinationEwalletUserNotFoundException("Destination e-wallet user not found for id " + request.getIdEwallet() + " and phone number " + request.getDestinationPhoneNumber()));

        String referenceNumber = TransactionUtils.generateReferenceNumber("TOP");
        String uniqueCode = TransactionUtils.generateUniqueCode(referenceNumber);

        Transaction transaction = Transaction.builder()
                .user(user)
                .ewallet(destinationEwalletUser.getEwallet())
                .type(TransactionType.TOP_UP)
                .destinationPhoneNumber(destinationEwalletUser.getPhoneNumber())
                .amount(request.getAmount())
                .adminFee(ADMIN_FEE)
                .totalamount(totalAmount)
                .uniqueCode(uniqueCode)
                .referenceNumber(referenceNumber)
                .note(request.getNote())
                .time(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);

        user.setBalance(user.getBalance().subtract(totalAmount));
        destinationEwalletUser.setBalance(destinationEwalletUser.getBalance().add(request.getAmount()));
        userRepository.save(user);
        ewalletUserRepository.save(destinationEwalletUser);

        Optional<SavedEwalletUser> optionalSavedEwalletUser = savedEwalletUserRepository.findByOwnerAndEwalletUser(user, destinationEwalletUser);
        if (request.isSavedAccount() && optionalSavedEwalletUser.isEmpty()) {
            SavedEwalletUser savedEwalletUser = SavedEwalletUser.builder()
                    .owner(user)
                    .ewalletUser(destinationEwalletUser)
                    .favorite(false)
                    .transferCount(1)
                    .build();
            savedEwalletUserRepository.save(savedEwalletUser);
        } else {
            optionalSavedEwalletUser.ifPresent(ewalletUser -> {
                ewalletUser.setTransferCount(ewalletUser.getTransferCount() + 1);
                savedEwalletUserRepository.save(ewalletUser);
            });
        }

        Bank bank = user.getBank();
        String bankName = bank != null ? bank.getName() : "Unknown";

        Ewallet ewallet = ewalletRepository.findById(destinationEwalletUser.getEwallet().getId())
                .orElseThrow(() -> new TransactionExceptions.EwalletNotFoundException("E-wallet not found"));

        return TopUpResponse.builder()
                .user(TopUpResponse.UserDto.builder()
                        .accountNumber(user.getAccountNumber())
                        .name(user.getName())
                        .imagePath(user.getImagePath())
                        .bankName(bankName)
                        .build())
                .userEwallet(TopUpResponse.UserEwalletDto.builder()
                        .name(destinationEwalletUser.getName())
                        .phoneNumber(destinationEwalletUser.getPhoneNumber())
                        .imagePath(destinationEwalletUser.getImagePath())
                        .ewallet(TopUpResponse.EwalletDto.builder()
                                .name(ewallet.getName())
                                .build())
                        .build())
                .amount(transaction.getAmount())
                .adminFee(transaction.getAdminFee())
                .totalAmount(transaction.getTotalamount())
                .build();
    }

    @Override
    @Transactional
    public TransferResponse transferWithinBCA(User user, TransferRequest request) {
        if (request.getAmount().compareTo(MINIMUM_TRANSFER_AMOUNT) < 0) {
            throw new TransactionExceptions.InvalidTransferAmountException("Transfer amount must be at least " + MINIMUM_TRANSFER_AMOUNT);
        }

        if (request.getDestinationAccountNumber().equals(user.getAccountNumber())) {
            throw new TransactionExceptions.InvalidTransferDestinationException("Cannot transfer to your own account");
        }

        User destinationUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new TransactionExceptions.DestinationAccountNotFoundException("Destination account not found " + request.getDestinationAccountNumber()));

        validateMpin(request.getMpin(), user);

        BigDecimal totalAmount = request.getAmount();

        checkSufficientBalance(user, totalAmount);

        String referenceNumber = TransactionUtils.generateReferenceNumber("TRF");
        String uniqueCode = TransactionUtils.generateUniqueCode(referenceNumber);

        Transaction transaction = Transaction.builder()
                .user(user)
                .bank(user.getBank())
                .type(TransactionType.TRANSFER)
                .destinationAccountNumber(request.getDestinationAccountNumber())
                .amount(request.getAmount())
                .adminFee(BigDecimal.ZERO)
                .totalamount(totalAmount)
                .uniqueCode(uniqueCode)
                .referenceNumber(referenceNumber)
                .note(request.getNote())
                .time(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);

        String depositReferenceNumber = referenceNumber.replace("TRF", "DPT");
        String depositUniqueCode = uniqueCode.replace("TRF", "DPT");

        Transaction depositTransaction = Transaction.builder()
                .user(destinationUser)
                .bank(user.getBank())
                .type(TransactionType.DEPOSIT)
                .destinationAccountNumber(null)
                .amount(request.getAmount())
                .adminFee(BigDecimal.ZERO)
                .totalamount(request.getAmount())
                .uniqueCode(depositUniqueCode)
                .referenceNumber(depositReferenceNumber)
                .note(request.getNote())
                .time(LocalDateTime.now())
                .build();
        transactionRepository.save(depositTransaction);

        user.setBalance(user.getBalance().subtract(totalAmount));
        destinationUser.setBalance(destinationUser.getBalance().add(request.getAmount()));
        userRepository.save(user);
        userRepository.save(destinationUser);

        Optional<SavedAccount> optionalSavedAccount = savedAccountRepository.findByOwnerAndAccountNumber(user, destinationUser.getAccountNumber());

        if (request.isSavedAccount() && optionalSavedAccount.isEmpty()) {
            SavedAccount savedAccount = SavedAccount.builder()
                    .owner(user)
                    .bank(destinationUser.getBank())
                    .name(destinationUser.getName())
                    .accountNumber(destinationUser.getAccountNumber())
                    .imagePath(user.getImagePath())
                    .favorite(false)
                    .transferCount(1)
                    .build();
            savedAccountRepository.save(savedAccount);
        } else {
            optionalSavedAccount.ifPresent(account -> {
                account.setTransferCount(account.getTransferCount() + 1);
                savedAccountRepository.save(account);
            });
        }

        return TransferResponse.builder()
                .sourceUser(TransferResponse.UserDTO.builder()
                        .name(user.getName())
                        .bank(user.getBank().getName())
                        .accountNumber(user.getAccountNumber())
                        .imagePath(user.getImagePath())
                        .build())
                .destinationUser(TransferResponse.UserDTO.builder()
                        .name(destinationUser.getName())
                        .bank(destinationUser.getBank().getName())
                        .accountNumber(destinationUser.getAccountNumber())
                        .imagePath(destinationUser.getImagePath())
                        .build())
                .amount(request.getAmount())
                .adminFee(BigDecimal.ZERO)
                .totalAmount(totalAmount)
                .note(request.getNote())
                .build();
    }

    @Override
    public MonthlyReportResponse getMonthlyReport(User user, int month, int year) {
        if (month < 1 || month > 12) {
            throw new TransactionExceptions.InvalidMonthException("Invalid month. It must be between 1 and 12.");
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (year < 1900 || year > currentYear) {
            throw new TransactionExceptions.InvalidYearException("Invalid year. It must be between 1900 and " + currentYear + ".");
        }

        BigDecimal income = BigDecimal.valueOf(0);
        BigDecimal expense = BigDecimal.valueOf(0);

        List<Transaction> transactions = transactionRepository.findByUserAndMonthAndYear(user.getId(), month, year);
        for (Transaction transaction : transactions) {
          if (Objects.requireNonNull(transaction.getType()) == TransactionType.TRANSFER || transaction.getType() == TransactionType.TOP_UP) {
            expense = expense.add(transaction.getTotalamount());
          } else if (transaction.getType() == TransactionType.DEPOSIT) {
            income = income.add(transaction.getTotalamount());
          }
        }

        return MonthlyReportResponse.builder()
                .income(income)
                .expense(expense)
                .total(income.subtract(expense))
                .build();
    }

    @Override
    @Transactional
    public MerchantTransactionResponse merchantTransaction(User user, MerchantTransactionRequest request) {
        validateMpin(request.getMpin(), user);

        if (request.getAmount().compareTo(BigDecimal.ONE) < 0) {
            throw new TransactionExceptions.InvalidTransactionAmountException("Transaction amount must be at least 1 rupiah");
        }

        UUID merchantId;
        try {
            merchantId = UUID.fromString(String.valueOf(request.getIdQris()));
        } catch (IllegalArgumentException e) {
            throw new TransactionExceptions.MerchantNotFoundException("Invalid QRIS ID format: " + request.getIdQris());
        }

        Merchant destinationMerchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new TransactionExceptions.MerchantNotFoundException("Merchant not found with id " + request.getIdQris()));

        BigDecimal totalAmount = request.getAmount();

        checkSufficientBalance(user, totalAmount);

        String referenceNumber = TransactionUtils.generateReferenceNumber("MCH");
        String uniqueCode = TransactionUtils.generateUniqueCode(referenceNumber);

        Transaction transaction = Transaction.builder()
                .user(user)
                .destinationIdQris(destinationMerchant)
                .type(TransactionType.QRPAYMENT)
                .amount(request.getAmount())
                .adminFee(BigDecimal.ZERO)
                .totalamount(totalAmount)
                .uniqueCode(uniqueCode)
                .referenceNumber(referenceNumber)
                .note(request.getNote())
                .time(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);

        user.setBalance(user.getBalance().subtract(totalAmount));
        userRepository.save(user);

        Bank bank = user.getBank();
        String bankName = bank != null ? bank.getName() : "Unknown";

        return MerchantTransactionResponse.builder()
                .sourceUser(MerchantTransactionResponse.SourceUserDTO.builder()
                        .name(user.getName())
                        .bank(bankName)
                        .accountNumber(user.getAccountNumber())
                        .imagePath(user.getImagePath())
                        .build())
                .destinationUser(MerchantTransactionResponse.DestinationUserDTO.builder()
                        .name(destinationMerchant.getName())
                        .nameMerchant(destinationMerchant.getName())
                        .nmid(destinationMerchant.getNmid())
                        .terminalId(destinationMerchant.getTerminalId())
                        .imagePath(destinationMerchant.getImagePath())
                        .build())
                .amount(transaction.getAmount())
                .adminFee(transaction.getAdminFee())
                .totalAmount(transaction.getTotalamount())
                .note(transaction.getNote())
                .build();
    }

    @Override
    public Page<MutationResponse> getAllMutation(User user, MutationRequest request, int page, int size) {
        LocalDateTime startDateTime = request.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = request.getEndDate().atTime(LocalTime.MAX);

        String transactionCategory = request.getTransactionCategory().toString();

        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findByUserAndTimeBetweenAndTransactionCategory(
          user, startDateTime, endDateTime, transactionCategory, pageable);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return transactions.map(transaction -> MutationResponse.builder()
                .transactionId(transaction.getId())
                .uniqueCode(transaction.getUniqueCode().replaceAll("TRF-|DPT-|TOP-|MCH-", ""))
                .type(formatTransactionType(transaction))
                .totalAmount(transaction.getTotalamount())
                .time(transaction.getTime())
                .referenceNumber(transaction.getReferenceNumber().replaceAll("TRF-|DPT-|TOP-|MCH-", ""))
                .destinationAccountNumber(transaction.getDestinationAccountNumber())
                .destinationPhoneNumber(transaction.getDestinationPhoneNumber())
                .formattedDate(transaction.getTime().format(dateFormatter))
                .formattedTime(transaction.getTime().format(timeFormatter))
                .build());
    }

    @Override
    public MutationDetailResponse getMutationDetail(User user, UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionExceptions.TransactionNotFoundException("Transaction with ID " + transactionId + " not found"));

        if (user != transaction.getUser()) {
           throw new TransactionExceptions.TransactionNotOwnedByUser("Transaction is not owned by user");
        }

        MutationDetailResponse.MutationUser sender = null;
        MutationDetailResponse.MutationUser receiver = null;

        MutationDetailResponse.MutationUser mutationUser = MutationDetailResponse.MutationUser.builder()
                .name(user.getName())
                .accountNumber(user.getAccountNumber())
                .imagePath(user.getImagePath())
                .vendorName("Tahapan BCA")
                .build();

        if (transaction.getType() == TransactionType.TOP_UP) {
            sender = mutationUser;

            EwalletUser ewalletUser = ewalletUserRepository.findByPhoneNumberAndEwallet(transaction.getDestinationPhoneNumber(), transaction.getEwallet())
                    .orElseThrow(() -> new TransactionExceptions.DestinationEwalletUserNotFoundException("not found eWalletUser with number " + transaction.getDestinationPhoneNumber() + " and eWalletId" + transaction.getEwallet().getId()));

            receiver = MutationDetailResponse.MutationUser.builder()
                    .name(ewalletUser.getName())
                    .accountNumber(transaction.getDestinationPhoneNumber())
                    .imagePath(ewalletUser.getImagePath())
                    .vendorName(ewalletUser.getEwallet().getName())
                    .build();
        } else if (transaction.getType() == TransactionType.TRANSFER) {
            sender = mutationUser;

            User destinationUser = userRepository.findByAccountNumber(transaction.getDestinationAccountNumber())
                    .orElseThrow(() -> new TransactionExceptions.UserNotFoundException("User with account number " + transaction.getDestinationAccountNumber() + " not found"));

            receiver = MutationDetailResponse.MutationUser.builder()
                    .name(destinationUser.getName())
                    .accountNumber(transaction.getDestinationAccountNumber())
                    .imagePath(destinationUser.getImagePath())
                    .vendorName(transaction.getBank().getName())
                    .build();
        } else if (transaction.getType() == TransactionType.DEPOSIT) {
            String referenceNumber = transaction.getReferenceNumber().replace("DPT", "TRF");

            Transaction transfer = transactionRepository.findByReferenceNumber(referenceNumber)
                    .orElseThrow(() -> new TransactionExceptions.TransactionNotFoundException("Transaction with reference number " + referenceNumber + " not found"));

            User sourceUser = transfer.getUser();

            sender = MutationDetailResponse.MutationUser.builder()
                    .name(sourceUser.getName())
                    .accountNumber(sourceUser.getAccountNumber())
                    .imagePath(sourceUser.getImagePath())
                    .vendorName(transaction.getBank().getName())
                    .build();

            receiver = mutationUser;
        }

        return MutationDetailResponse.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(transaction.getAmount())
                .adminFee(transaction.getAdminFee())
                .totalAmount(transaction.getTotalamount())
                .build();
    }

    private void validateMpin(String mpin, User user) {
        if (!passwordEncoder.matches(mpin, user.getMpin())) {
            throw new TransactionExceptions.InvalidMpinException("Invalid MPIN");
        }
    }

    private void checkSufficientBalance(User user, BigDecimal totalAmount) {
        if (user.getBalance().compareTo(totalAmount) < 0) {
            throw new TransactionExceptions.InsufficientBalanceException("Insufficient balance");
        }
    }

    private String formatTransactionType(Transaction transaction) {
        return switch (transaction.getType()) {
            case TRANSFER -> "Transfer M-Banking DB";
            case TOP_UP -> ewalletRepository.findById(transaction.getEwallet().getId())
                    .map(ewallet -> "TOP UP " + ewallet.getName())
                    .orElse("Unknown Wallet");
            default -> transaction.getType().toString();
        };
    }
}
