package org.synrgy.setara.transaction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.synrgy.setara.common.utils.TransactionUtils;
import org.synrgy.setara.contact.model.SavedEwalletUser;
import org.synrgy.setara.contact.repository.SavedEwalletUserRepository;
import org.synrgy.setara.transaction.dto.TransactionRequest;
import org.synrgy.setara.transaction.dto.TransactionResponse;
import org.synrgy.setara.transaction.exception.TransactionExceptions;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.model.TransactionType;
import org.synrgy.setara.transaction.repository.TransactionRepository;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.model.Bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EwalletUserRepository ewalletUserRepository;
    private final SavedEwalletUserRepository savedEwalletUserRepository;
    private final PasswordEncoder passwordEncoder;
    private static final BigDecimal ADMIN_FEE = BigDecimal.valueOf(1000);
    private static final BigDecimal MINIMUM_TOP_UP_AMOUNT = BigDecimal.valueOf(10000);

    @Override
    public TransactionResponse topUp(TransactionRequest request, String token) {
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
            saveEwalletUser(user, destinationEwalletUser);
        }

        return createTransactionResponse(transaction, destinationEwalletUser);
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

    private Transaction createTransaction(TransactionRequest request, User user, EwalletUser destinationEwalletUser, BigDecimal totalAmount) {
        String referenceNumber = TransactionUtils.generateReferenceNumber();
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
        SavedEwalletUser savedEwalletUser = SavedEwalletUser.builder()
                .owner(owner)
                .ewalletUser(ewalletUser)
                .favorite(false)
                .build();
        savedEwalletUserRepository.save(savedEwalletUser);
    }

    private TransactionResponse createTransactionResponse(Transaction transaction, EwalletUser destinationEwalletUser) {
        Bank bank = transaction.getUser().getBank();
        String bankName = bank != null ? bank.getName() : "Unknown";

        return TransactionResponse.builder()
                .user(TransactionResponse.UserDto.builder()
                        .accountNumber(transaction.getUser().getAccountNumber())
                        .name(transaction.getUser().getName())
                        .imagePath(transaction.getUser().getImagePath())
                        .bankName(bankName)
                        .build())
                .userEwallet(TransactionResponse.UserEwalletDto.builder()
                        .name(destinationEwalletUser.getName())
                        .phoneNumber(destinationEwalletUser.getPhoneNumber())
                        .imagePath(destinationEwalletUser.getImagePath())
                        .ewallet(TransactionResponse.EwalletDto.builder()
                                .name(transaction.getEwallet().getName())
                                .build())
                        .build())
                .amount(transaction.getAmount())
                .totalAmount(transaction.getTotalamount())
                .adminFee(transaction.getAdminFee())
                .build();
    }
}