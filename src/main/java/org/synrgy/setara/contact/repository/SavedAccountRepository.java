package org.synrgy.setara.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.contact.model.SavedAccount;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SavedAccountRepository extends JpaRepository<SavedAccount, UUID> {

  @Modifying
  @Query("UPDATE SavedAccount sa SET sa.deletedAt = CURRENT_TIMESTAMP WHERE sa.id = :id")
  void deactivateById(UUID id);

  @Modifying
  @Query("UPDATE SavedAccount sa SET sa.deletedAt = null WHERE sa.id = :id")
  void restoreById(@Param("id") UUID id);

  @Query("SELECT sa FROM SavedAccount sa WHERE sa.owner.id = :ownerId AND (:favOnly = false OR sa.favorite = true)")
  List<SavedAccount> fetchAll(UUID ownerId, boolean favOnly);

  @Modifying
  @Query("UPDATE SavedAccount sa SET sa.favorite = :isFavorite WHERE sa.id = :id")
  void putFavorite(@Param("id") UUID id, @Param("isFavorite") boolean isFavorite);

  @Query("SELECT COUNT(s) FROM SavedAccount s WHERE s.owner.id = :ownerId AND s.favorite = true AND s.deletedAt IS NULL")
  Long countFavoriteByOwner(@Param("ownerId") UUID ownerId);

  @Query("SELECT COUNT(s) FROM SavedAccount s WHERE s.owner.id = :ownerId AND s.favorite = false AND s.deletedAt IS NULL")
  Long countNotFavoriteByOwner(@Param("ownerId") UUID ownerId);

  List<SavedAccount> findByOwnerId(UUID id);

  Optional<SavedAccount> findByOwnerAndAccountNumber(User owner, String accountNumber);
}
