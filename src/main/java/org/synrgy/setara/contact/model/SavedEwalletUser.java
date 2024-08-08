package org.synrgy.setara.contact.model;

import jakarta.persistence.*;
import lombok.*;
import org.synrgy.setara.common.model.Auditable;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_saved_ewallet_users")
public class SavedEwalletUser extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "owner_id",
    nullable = false,
    referencedColumnName = "id"
  )
  private User owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "ewallet_user_id",
    nullable = false,
    referencedColumnName = "id"
  )
  private EwalletUser ewalletUser;

  @Column(
    name = "is_favorite",
    columnDefinition = "boolean default false"
  )
  private boolean favorite;

}
