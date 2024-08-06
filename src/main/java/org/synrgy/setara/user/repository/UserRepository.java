package org.synrgy.setara.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.user.model.User;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  @Modifying
  @Query("UPDATE User u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
  void deactivateById(@Param("id") UUID id);

  @Modifying
  @Query("UPDATE User u SET u.deletedAt = null WHERE u.id = :id")
  void restoreById(@Param("id") UUID id);

  Optional<User> findByEmail(String email);

  Optional<User> findBySignature(String signature);

  Optional<User> findByName(String name);

  Optional<User> findByMpin(String mpin);

  boolean existsByEmail(String email);

  boolean existsBySignature(String signature);

  boolean existsByAccountNumber(String accountNumber);

  boolean existsByPhoneNumber(String phoneNumber);

  boolean existsByNik(String nik);

  Optional<User> findByAccountNumber(String destinationAccountNumber);

  @Modifying
  @Query("UPDATE User u SET u.balance = :balance WHERE u.id = :id")
  void updateBalanceById(@Param("id") UUID id, @Param("balance") BigDecimal balance);

  @Modifying
  @Query("UPDATE User u SET u.balance = :balance WHERE u.accountNumber = :account_number")
  void updateBalanceByAccountNumber(@Param("account_number") String accountNumber, @Param("balance") BigDecimal balance);

}
