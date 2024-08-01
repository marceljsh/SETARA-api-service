package org.synrgy.setara.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.vendor.model.Merchant;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

    @Modifying
    @Query("UPDATE Ewallet e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = :id")
    void deactivateById(UUID id);

    @Modifying
    @Query("UPDATE Ewallet e SET e.deletedAt = null WHERE e.id = :id")
    void restoreById(@Param("id") UUID id);

    Optional<Merchant> findByQrisCode(String qrisCode);
    Optional<Merchant> findById(UUID id);

    Optional<Merchant> findByName(String name);
    Optional<Merchant> findByNmid(String nmid);
    Optional<Merchant> findByTerminalId(String terminalId);
}
