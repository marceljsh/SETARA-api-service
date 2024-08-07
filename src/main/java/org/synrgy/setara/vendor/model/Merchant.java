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
@Table(name = "tbl_merchants")
public class Merchant extends Auditable {

    private String name;

    @Column(unique = true)
    private String terminalId;

    @Column(unique = true)
    private String nmid;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "qris_code", columnDefinition = "TEXT", length = 512)
    private String qrisCode;

    private String address;
}
