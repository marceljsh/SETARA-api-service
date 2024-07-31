package org.synrgy.setara.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.vendor.model.Merchant;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

  @Modifying
  @Query("UPDATE Merchant m " +
      "SET m.updatedAt = CURRENT_TIMESTAMP " +
      "WHERE m.id = :id")
  void deactivateById(UUID id);

  @Modifying
  @Query("UPDATE Merchant m " +
      "SET m.updatedAt = null " +
      "WHERE m.id = :id")
  void restoreById(UUID id);

  boolean existsByQrisCode(String qrisCode);

  Optional<Merchant> findByQrisCode(String qrisCode);

  boolean existsByNmid(String nmid);

  Optional<Merchant> findByNmid(String nmid);

  boolean existsByTerminalId(String terminalId);

  Optional<Merchant> findByTerminalId(String terminalId);

}
