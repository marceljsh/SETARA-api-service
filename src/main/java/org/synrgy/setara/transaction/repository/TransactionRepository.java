package org.synrgy.setara.transaction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  @Modifying
  @Query("UPDATE Transaction t SET t.deletedAt = CURRENT_TIMESTAMP WHERE t.id = :id")
  void deactivateById(@Param("id") UUID id);

  @Modifying
  @Query("UPDATE Transaction t SET t.deletedAt = null WHERE t.id = :id")
  void restoreById(@Param("id") UUID id);

  @Query(value = "SELECT * FROM tbl_transactions t WHERE t.user_id = :userId AND EXTRACT(MONTH FROM t.time) = :month AND EXTRACT(YEAR FROM t.time) = :year", nativeQuery = true)
  List<Transaction> findByUserAndMonthAndYear(@Param("userId") UUID userId, @Param("month") int month, @Param("year") int year);

  @Query(value = "SELECT * FROM tbl_transactions t WHERE t.destination_phone_number = :phoneNumber AND EXTRACT(MONTH FROM t.time) = :month AND EXTRACT(YEAR FROM t.time) = :year AND t.type = 'TRANSFER'", nativeQuery = true)
  List<Transaction> findTransfersByPhoneNumberAndMonthAndYear(@Param("phoneNumber") String phoneNumber, @Param("month") int month, @Param("year") int year);

  @Query(value = "SELECT * FROM tbl_transactions t WHERE t.destination_account_number = :accountNumber AND EXTRACT(MONTH FROM t.time) = :month AND EXTRACT(YEAR FROM t.time) = :year AND t.type = 'TRANSFER'", nativeQuery = true)
  List<Transaction> findTransfersByAccountNumberAndMonthAndYear(@Param("accountNumber") String accountNumber, @Param("month") int month, @Param("year") int year);

  @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.time BETWEEN :startDate AND :endDate AND " +
          "(:transactionCategory = 'ALL_TRANSACTIONS' OR " +
          "(:transactionCategory = 'OUTGOING' AND t.type IN ('TRANSFER', 'TOP_UP')) OR " +
          "(:transactionCategory = 'INCOMING' AND t.type = 'DEPOSIT'))")
  Page<Transaction> findByUserAndTimeBetweenAndTransactionCategory(@Param("user") User user,
                                                                   @Param("startDate") LocalDateTime startDate,
                                                                   @Param("endDate") LocalDateTime endDate,
                                                                   @Param("transactionCategory") String transactionCategory,
                                                                   Pageable pageable);

  Optional<Transaction> findByReferenceNumber(String referenceNumber);
}
