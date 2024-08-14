package org.synrgy.setara.vendor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.synrgy.setara.common.model.Auditable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_merchants")
public class Merchant extends Auditable {

  private String name;

  @Column(
    unique = true,
    length = 3
  )
  private String terminalId;

  @Column(
    unique = true,
    length = 15
  )
  private String nmid;

  private String address;

  @Column(
    unique = true,
    columnDefinition = "TEXT",
    length = 512
  )
  private String qrisCode;

  private String imagePath;

}
