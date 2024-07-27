package org.synrgy.setara.user.model;

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
import org.synrgy.setara.vendor.model.Ewallet;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_ewallet_users")
public class EwalletUser extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "ewallet_id",
    nullable = false,
    referencedColumnName = "id"
  )
  private Ewallet ewallet;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String phoneNumber;

  private BigDecimal balance;

  private String imagePath;

}
