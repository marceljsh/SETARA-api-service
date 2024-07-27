package org.synrgy.setara.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.vendor.model.Bank;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankRepository extends JpaRepository<Bank, UUID> {

  @Modifying
  @Query("UPDATE Bank b SET b.deletedAt = CURRENT_TIMESTAMP WHERE b.id = :id")
  void deactivateById(UUID id);

  @Modifying
  @Query("UPDATE Bank b SET b.deletedAt = null WHERE b.id = :id")
  void restoreById(@Param("id") UUID id);

  @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END FROM Bank b WHERE b.name = :name")
  boolean existsByName(@Param("name") String name);

  Optional<Bank> findByName(String name);
}
