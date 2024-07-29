package org.synrgy.setara.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.contact.model.EwalletContact;

import java.util.List;
import java.util.UUID;

@Repository
public interface EwalletContactRepository extends JpaRepository<EwalletContact, UUID> {

  @Modifying
  @Query("UPDATE EwalletContact ec " +
          "SET ec.deletedAt = CURRENT_TIMESTAMP " +
          "WHERE ec.id = :id")
  void archiveById(@Param("id") UUID id);

  @Modifying
  @Query("UPDATE EwalletContact ec " +
          "SET ec.deletedAt = null " +
          "WHERE ec.id = :id")
  void restoreById(@Param("id") UUID id);

  @Query("SELECT ec FROM EwalletContact ec " +
          "WHERE ec.owner.id = :owner_id " +
          "AND (:fav_only = false OR ec.favorite = true)")
  List<EwalletContact> fetchAllByOwnerId(@Param("owner_id") UUID ownerId, @Param("fav_only") boolean favOnly);

  @Query("SELECT ec FROM EwalletContact ec " +
          "WHERE ec.owner.id = :ownerId " +
          "AND ec.ewalletUser.ewallet.id = :ewallet_id " +
          "AND (:fav_only = false OR ec.favorite = true)")
  List<EwalletContact> findByOwnerIdAndEwalletName(@Param("owner_id") UUID ownerId, @Param("ewallet_id") UUID ewalletId, @Param("fav_only") boolean favOnly);

  @Modifying
  @Query("UPDATE EwalletContact ec SET ec.favorite = :favorite WHERE ec.id = :id")
  void updateFavorite(@Param("id") UUID id, @Param("favorite") boolean favorite);
}
