package org.synrgy.setara.contact.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.contact.model.SavedEwalletUser;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface SavedEwalletUserRepository extends JpaRepository<SavedEwalletUser, UUID> {

  Logger log = LoggerFactory.getLogger(SavedEwalletUserRepository.class);

  @Modifying
  @Query("UPDATE SavedEwalletUser su SET su.deletedAt = CURRENT_TIMESTAMP WHERE su.id = :id")
  void deactivateById(UUID id);

  @Modifying
  @Query("UPDATE SavedEwalletUser su SET su.deletedAt = null WHERE su.id = :id")
  void restoreById(@Param("id") UUID id);

  boolean existsByOwnerAndEwalletUser(User owner, EwalletUser ewalletUser);

  List<SavedEwalletUser> findByOwnerId(UUID id);

  List<SavedEwalletUser> findByOwnerIdAndFavorite(UUID ownerId, boolean favorite);

  @Query("SELECT s FROM SavedEwalletUser s " +
          "WHERE s.owner.id = :ownerId " +
          "AND (:ewalletName IS NULL OR s.ewalletUser.ewallet.name = :ewalletName)")
  List<SavedEwalletUser> findByOwnerIdAndEwalletName(
          @Param("ownerId") UUID ownerId,
          @Param("ewalletName") String ewalletName);

  @Query("SELECT COUNT(s) FROM SavedEwalletUser s WHERE s.owner.id = :ownerId AND s.favorite = true AND s.deletedAt IS NULL")
  Long countFavoriteByOwner(@Param("ownerId") UUID ownerId);

  @Query("SELECT COUNT(s) FROM SavedEwalletUser s WHERE s.owner.id = :ownerId AND s.favorite = false AND s.deletedAt IS NULL")
  Long countNotFavoriteByOwner(@Param("ownerId") UUID ownerId);

  @Modifying
  @Query("UPDATE SavedEwalletUser sa SET sa.favorite = :isFavorite WHERE sa.id = :id")
  void putFavorite(@Param("id") UUID id, @Param("isFavorite") boolean isFavorite);
}
