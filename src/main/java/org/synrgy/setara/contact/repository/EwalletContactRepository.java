package org.synrgy.setara.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.contact.model.EwalletContact;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.vendor.model.Ewallet;

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
          "WHERE ec.owner = :owner " +
          "AND ec.ewalletUser.ewallet = :ewallet " +
          "AND (:fav_only = false OR ec.favorite = true)")
  List<EwalletContact> fetchAllByOwnerAndEwallet(@Param("owner") User owner, @Param("ewallet") Ewallet ewallet, @Param("fav_only") boolean favOnly);

  @Modifying
  @Query("UPDATE EwalletContact ec " +
      "SET ec.favorite = :favorite " +
      "WHERE ec.id = :id")
  void updateFavorite(@Param("id") UUID id, @Param("favorite") boolean favorite);

  @Query("SELECT COUNT(ec) > 0 FROM EwalletContact ec " +
          "WHERE ec.id = :id AND ec.owner = :owner")
  boolean belongsToOwner(@Param("ownerId") User owner, @Param("id") UUID id);

  boolean existsByOwnerAndEwalletUser(User owner, EwalletUser user);

}
