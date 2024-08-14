package org.synrgy.setara.contact.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.synrgy.setara.common.model.Auditable;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.vendor.model.Bank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_bank_contacts")
public class BankContact extends Auditable {

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(
    name = "owner_id",
    nullable = false,
    referencedColumnName = "id"
  )
  private User owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "bank_id",
    nullable = false,
    referencedColumnName = "id"
  )
  private Bank bank;

  @Column(nullable = false)
  private String name;

  @Column(
    nullable = false,
    unique = true,
    length = 20
  )
  private String accountNumber;

  private String imagePath;

  @Column(
    name = "is_favorite",
    columnDefinition = "boolean default false"
  )
  private boolean favorite;

}
