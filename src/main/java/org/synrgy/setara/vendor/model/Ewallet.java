package org.synrgy.setara.vendor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.synrgy.setara.common.model.Auditable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
