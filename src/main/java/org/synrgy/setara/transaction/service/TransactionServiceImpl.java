package org.synrgy.setara.transaction.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.synrgy.setara.transaction.utils.TransactionUtils;
import org.synrgy.setara.contact.model.SavedAccount;
import org.synrgy.setara.contact.model.SavedEwalletUser;
import org.synrgy.setara.contact.repository.SavedAccountRepository;
import org.synrgy.setara.contact.repository.SavedEwalletUserRepository;
import org.synrgy.setara.transaction.dto.*;
import org.synrgy.setara.transaction.exception.TransactionExceptions;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.model.TransactionType;
import org.synrgy.setara.transaction.repository.TransactionRepository;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.model.Merchant;
import org.synrgy.setara.vendor.repository.EwalletRepository;
import org.synrgy.setara.vendor.repository.MerchantRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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
    private static final BigDecimal MINIMUM_TRANSFER_AMOUNT = BigDecimal.valueOf(10000);

    @Override
    @Transactional
    public TopUpResponse topUp(TopUpRequest request) {
        String signature = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findBySignature(signature)
                .orElseThrow(() -> new TransactionExceptions.UserNotFoundException("User with signature " + signature + " not found"));

        validateMpin(request.getMpin(), user);
        validateTopUpAmount(request.getAmount());

        BigDecimal totalAmount = request.getAmount().add(ADMIN_FEE);
        checkSufficientBalance(user, totalAmount);

        EwalletUser destinationEwalletUser = ewalletUserRepository.findByPhoneNumber(request.getDestinationPhoneNumber())
                .orElseThrow(() -> new TransactionExceptions.DestinationEwalletUserNotFoundException("Destination e-wallet user not found for phone number " + request.getDestinationPhoneNumber()));

        Transaction transaction = createTransaction(request, user, destinationEwalletUser, totalAmount);

        updateBalances(user, destinationEwalletUser, request.getAmount(), totalAmount);
        transactionRepository.save(transaction);

        if (request.isSavedAccount()) {
            saveEwalletUser(user, destinationEwalletUser); // Save the ewallet user data
        }

        return createTransactionResponse(transaction, destinationEwalletUser);
    }

    @Override
    @Transactional
    public TransferResponse transferWithinBCA(TransferRequest request) {
        String signature = SecurityContextHolder.getContext().getAuthentication().getName();
        User sourceUser = userRepository.findBySignature(signature)
                .orElseThrow(() -> new TransactionExceptions.UserNotFoundException("User with signature " + signature + " not found"));

        if (request.getAmount().compareTo(MINIMUM_TRANSFER_AMOUNT) < 0) {
            throw new TransactionExceptions.InvalidTransferAmountException("Transfer amount must be at least " + MINIMUM_TRANSFER_AMOUNT);
        }

        if (request.getDestinationAccountNumber().equals(sourceUser.getAccountNumber())) {
            throw new TransactionExceptions.InvalidTransferDestinationException("Cannot transfer to your own account");
        }

        User destinationUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new TransactionExceptions.DestinationAccountNotFoundException("Destination account not found " + request.getDestinationAccountNumber()));

        validateMpin(request.getMpin(), sourceUser);

        BigDecimal totalAmount = request.getAmount();

        checkSufficientBalance(sourceUser, totalAmount);

        String referenceNumber = TransactionUtils.generateReferenceNumber("TRF");
        String uniqueCode = TransactionUtils.generateUniqueCode(referenceNumber);

        Transaction transaction = Transaction.builder()
                .user(sourceUser)
                .bank(sourceUser.getBank())
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

        String depositReferenceNumber = TransactionUtils.generateReferenceNumber("DPT");
        String depositUniqueCode = TransactionUtils.generateUniqueCode(depositReferenceNumber);

        Transaction depositTransaction = Transaction.builder()
                .user(destinationUser)
                .bank(sourceUser.getBank())  // Assuming the bank of the transaction is the bank of the source user
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

        sourceUser.setBalance(sourceUser.getBalance().subtract(totalAmount));
        destinationUser.setBalance(destinationUser.getBalance().add(request.getAmount()));
        userRepository.save(sourceUser);
        userRepository.save(destinationUser);

        if (request.isSavedAccount()) {
            SavedAccount savedAccount = SavedAccount.builder()
                    .owner(sourceUser)
                    .bank(destinationUser.getBank())
                    .name(destinationUser.getName())
                    .accountNumber(destinationUser.getAccountNumber())
                    .imagePath(sourceUser.getImagePath())
                    .favorite(false)
                    .build();
            savedAccountRepository.save(savedAccount);
        }

        return TransferResponse.builder()
                .sourceUser(TransferResponse.UserDTO.builder()
                        .name(sourceUser.getName())
                        .bank(sourceUser.getBank().getName())
                        .accountNumber(sourceUser.getAccountNumber())
                        .imagePath(sourceUser.getImagePath())
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

    private void validateMpin(String mpin, User user) {
        if (!passwordEncoder.matches(mpin, user.getMpin())) {
            throw new TransactionExceptions.InvalidMpinException("Invalid MPIN");
        }
    }

    private void validateTopUpAmount(BigDecimal amount) {
        if (amount.compareTo(MINIMUM_TOP_UP_AMOUNT) < 0) {
            throw new TransactionExceptions.InvalidTopUpAmountException("Top-up amount must be at least " + MINIMUM_TOP_UP_AMOUNT);
        }
    }

    private void checkSufficientBalance(User user, BigDecimal totalAmount) {
        if (user.getBalance().compareTo(totalAmount) < 0) {
            throw new TransactionExceptions.InsufficientBalanceException("Insufficient balance");
        }
    }

    private Transaction createTransaction(TopUpRequest request, User user, EwalletUser destinationEwalletUser, BigDecimal totalAmount) {
        String referenceNumber = TransactionUtils.generateReferenceNumber("TOP");
        String uniqueCode = TransactionUtils.generateUniqueCode(referenceNumber);

        return Transaction.builder()
                .user(user)
                .ewallet(destinationEwalletUser.getEwallet())
                .type(TransactionType.TOP_UP)
                .destinationPhoneNumber(request.getDestinationPhoneNumber())
                .amount(request.getAmount())
                .adminFee(ADMIN_FEE)
                .totalamount(totalAmount)
                .uniqueCode(uniqueCode)
                .referenceNumber(referenceNumber)
                .note(request.getNote())
                .time(LocalDateTime.now())
                .build();
    }

    private void updateBalances(User user, EwalletUser destinationEwalletUser, BigDecimal amount, BigDecimal totalAmount) {
        user.setBalance(user.getBalance().subtract(totalAmount));
        destinationEwalletUser.setBalance(destinationEwalletUser.getBalance().add(amount));
        userRepository.save(user);
        ewalletUserRepository.save(destinationEwalletUser);
    }

    private void saveEwalletUser(User owner, EwalletUser ewalletUser) {
        if (!savedEwalletUserRepository.existsByOwnerAndEwalletUser(owner, ewalletUser)) {
            SavedEwalletUser savedEwalletUser = SavedEwalletUser.builder()
                    .owner(owner)
                    .ewalletUser(ewalletUser)
                    .favorite(false)
                    .build();
            savedEwalletUserRepository.save(savedEwalletUser);
        }
    }

    private TopUpResponse createTransactionResponse(Transaction transaction, EwalletUser destinationEwalletUser) {
        Bank bank = transaction.getUser().getBank();
        String bankName = bank != null ? bank.getName() : "Unknown";

        return TopUpResponse.builder()
                .user(TopUpResponse.UserDto.builder()
                        .accountNumber(transaction.getUser().getAccountNumber())
                        .name(transaction.getUser().getName())
                        .imagePath(transaction.getUser().getImagePath())
                        .bankName(bankName)
                        .build())
                .userEwallet(TopUpResponse.UserEwalletDto.builder()
                        .name(destinationEwalletUser.getName())
                        .phoneNumber(destinationEwalletUser.getPhoneNumber())
                        .imagePath(destinationEwalletUser.getImagePath())
                        .build())
                .amount(transaction.getAmount())
                .adminFee(transaction.getAdminFee())
                .totalAmount(transaction.getTotalamount())
                .build();
    }

    @Override
    public MonthlyReportResponse getMonthlyReport(int month, int year) {
        validateMonth(month);
        validateYear(year);

        String signature = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findBySignature(signature)
                .orElseThrow(() -> new TransactionExceptions.UserNotFoundException("User with signature " + signature + " not found"));

        BigDecimal income = BigDecimal.valueOf(0);
        BigDecimal expense = BigDecimal.valueOf(0);

        List<Transaction> transactions = transactionRepository.findByUserAndMonthAndYear(user.getId(), month, year);
        for (Transaction transaction : transactions) {
            switch (transaction.getType()) {
                case TRANSFER, TOP_UP:
                    expense = expense.add(transaction.getTotalamount());
                    break;
                case DEPOSIT:
                    income = income.add(transaction.getTotalamount());
                    break;
            }
        }

        List<Transaction> transfersThroughPhoneNumber = transactionRepository.findTransfersByPhoneNumberAndMonthAndYear(user.getPhoneNumber(), month, year);
        for (Transaction transfer : transfersThroughPhoneNumber) {
            income = income.add(transfer.getAmount());
        }

        List<Transaction> transfersThroughAccountNumber = transactionRepository.findTransfersByAccountNumberAndMonthAndYear(user.getAccountNumber(), month, year);
        for (Transaction transfer : transfersThroughAccountNumber) {
            income = income.add(transfer.getAmount());
        }

        return MonthlyReportResponse.builder()
                .income(income)
                .expense(expense)
                .total(income.subtract(expense))
                .build();
    }

    private void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new TransactionExceptions.InvalidMonthException("Invalid month. It must be between 1 and 12.");
        }
    }

    private void validateYear(int year) {
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        if (year < 1900 || year > currentYear) {
            throw new TransactionExceptions.InvalidYearException("Invalid year. It must be between 1900 and " + currentYear + ".");
        }
    }

    @Override
    @Transactional
    public MerchantTransactionResponse merchantTransaction(MerchantTransactionRequest request) {
        try {
            String signature = SecurityContextHolder.getContext().getAuthentication().getName();
            User sourceUser = userRepository.findBySignature(signature)
                    .orElseThrow(() -> new TransactionExceptions.UserNotFoundException("User with signature " + signature + " not found"));

            validateMpin(request.getMpin(), sourceUser);

            UUID merchantId;
            try {
                merchantId = UUID.fromString(String.valueOf(request.getIdQris()));
            } catch (IllegalArgumentException e) {
                throw new TransactionExceptions.MerchantNotFoundException("Invalid QRIS ID format: " + request.getIdQris());
            }

            Merchant destinationMerchant = merchantRepository.findById(merchantId)
                    .orElseThrow(() -> new TransactionExceptions.MerchantNotFoundException("Merchant not found with id " + request.getIdQris()));

            BigDecimal totalAmount = request.getAmount();

            checkSufficientBalance(sourceUser, totalAmount);

            String referenceNumber = TransactionUtils.generateReferenceNumber("MERCH");
            String uniqueCode = TransactionUtils.generateUniqueCode(referenceNumber);

            Transaction transaction = Transaction.builder()
                    .user(sourceUser)
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

            sourceUser.setBalance(sourceUser.getBalance().subtract(totalAmount));
            userRepository.save(sourceUser);

            return createTransactionResponse(transaction, sourceUser, destinationMerchant);
        } catch (Exception e) {
            log.error("Error processing merchant transaction", e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    private MerchantTransactionResponse createTransactionResponse(Transaction transaction, User sourceUser, Merchant destinationMerchant) {
        Bank bank = sourceUser.getBank();
        String bankName = bank != null ? bank.getName() : "Unknown";

        return MerchantTransactionResponse.builder()
                .sourceUser(MerchantTransactionResponse.SourceUserDTO.builder()
                        .name(sourceUser.getName())
                        .bank(bankName)
                        .accountNumber(sourceUser.getAccountNumber())
                        .imagePath(sourceUser.getImagePath())
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
    public Page<MutationResponse> getAllMutation(MutationRequest request, int page, int size) {
        String signature = SecurityContextHolder.getContext().getAuthentication().getName();
        User sourceUser = userRepository.findBySignature(signature)
                .orElseThrow(() -> new TransactionExceptions.UserNotFoundException("User with signature " + signature + " not found"));

        LocalDateTime startDateTime = request.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = request.getEndDate().atTime(LocalTime.MAX);

        String transactionCategory = request.getTransactionCategory().toString();

        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findByUserAndTimeBetweenAndTransactionCategory(
                sourceUser, startDateTime, endDateTime, transactionCategory, pageable);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return transactions.map(transaction -> MutationResponse.builder()
                .uniqueCode(transaction.getUniqueCode().replaceAll("TRF-|DPT-", ""))
                .type(formatTransactionType(transaction))
                .totalAmount(transaction.getTotalamount())
                .time(transaction.getTime())
                .referenceNumber(transaction.getReferenceNumber().replaceAll("TRF-|DPT-", ""))
                .destinationAccountNumber(transaction.getDestinationAccountNumber())
                .destinationPhoneNumber(transaction.getDestinationPhoneNumber())
                .formattedDate(transaction.getTime().format(dateFormatter))
                .formattedTime(transaction.getTime().format(timeFormatter))
                .build());
    }

    private String formatTransactionType(Transaction transaction) {
        switch (transaction.getType()) {
            case TRANSFER:
                return "Transfer M-Banking DB";
            case TOP_UP:
                return ewalletRepository.findById(transaction.getEwallet().getId())
                        .map(ewallet -> "TOP UP " + ewallet.getName())
                        .orElse("Unknown Wallet");
            default:
                return transaction.getType().toString();
        }
    }
}

