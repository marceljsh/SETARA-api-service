package org.synrgy.setara.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.synrgy.setara.contact.model.BankContact;
import org.synrgy.setara.user.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankContactRepository extends JpaRepository<BankContact, UUID> {

  @Modifying
  @Query("UPDATE BankContact ba " +
          "SET ba.deletedAt = CURRENT_TIMESTAMP " +
          "WHERE ba.id = :id")
  void archiveById(@Param("id") UUID id);

  @Modifying
  @Query("UPDATE BankContact ba " +
          "SET ba.deletedAt = null " +
          "WHERE ba.id = :id")
  void restoreById(@Param("id") UUID id);

  @Query("SELECT ba FROM BankContact ba " +
          "WHERE ba.owner = :owner AND " +
          "(:fav_only = false OR ba.favorite = true)")
  List<BankContact> fetchAllByOwner(@Param("owner") User owner, @Param("fav_only") boolean favOnly);

  @Modifying
  @Query("UPDATE BankContact ba " +
          "SET ba.favorite = :favorite " +
          "WHERE ba.id = :id")
  void updateFavorite(@Param("id") UUID id, @Param("favorite") boolean favorite);

  @Query("SELECT COUNT(ba) > 0 FROM BankContact ba " +
          "WHERE ba.id = :id AND ba.owner = :owner")
  boolean belongsToOwner(@Param("owner") User owner, @Param("id") UUID id);

}
