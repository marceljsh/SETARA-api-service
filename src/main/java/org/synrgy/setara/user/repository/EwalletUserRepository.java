package org.synrgy.setara.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.user.model.EwalletUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EwalletUserRepository extends JpaRepository<EwalletUser, UUID> {

  @Modifying
  @Query("UPDATE EwalletUser eu SET eu.deletedAt = CURRENT_TIMESTAMP WHERE eu.id = :id")
  void deactivateById(@Param("id") UUID id);

  @Modifying
  @Query("UPDATE EwalletUser eu SET eu.deletedAt = null WHERE eu.id = :id")
  void restoreById(@Param("id") UUID id);

  Optional<EwalletUser> findByPhoneNumber(String phoneNumber);
}
