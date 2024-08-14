package org.synrgy.setara.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.model.TransactionType;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

  @Modifying
  @Query("UPDATE Transaction t " +
          "SET t.deletedAt = CURRENT_TIMESTAMP " +
          "WHERE t.id = :id")
  void deactivateById(@Param("id") UUID id);

  @Modifying
  @Query("UPDATE Transaction t " +
          "SET t.deletedAt = null " +
          "WHERE t.id = :id")
  void restoreById(@Param("id") UUID id);

  boolean existsByReferenceNumber(String referenceNumber);

  @Query("SELECT t FROM Transaction t " +
          "WHERE t.user.id = :user_id AND " +
          "t.type IN :types AND " +
          "FUNCTION('MONTH', t.transactionTime) = :month AND " +
          "FUNCTION('YEAR', t.transactionTime) = :year")
  List<Transaction> findMonthlyReportMultipleTypes(@Param("user_id") UUID userId,
                                                   @Param("types") List<TransactionType> types,
                                                   @Param("month") int month,
                                                   @Param("year") int year);

  @Query("SELECT t FROM Transaction t " +
          "WHERE t.user.id = :user_id AND " +
          "t.type = :type AND " +
          "FUNCTION('MONTH', t.transactionTime) = :month AND " +
          "FUNCTION('YEAR', t.transactionTime) = :year")
  List<Transaction> findMonthlyReportSingleTypes(@Param("user_id") UUID userId,
                                                 @Param("type") TransactionType type,
                                                 @Param("month") int month,
                                                 @Param("year") int year);

}
