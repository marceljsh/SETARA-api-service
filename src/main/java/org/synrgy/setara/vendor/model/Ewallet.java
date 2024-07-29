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
@Table(name = "tbl_ewallets")
public class Ewallet extends Auditable {

  @Column(
    unique = true,
    nullable = false
  )
  private String name;

  @Column(nullable = false)
  private String imagePath;

}
