package org.synrgy.setara.vendor.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.vendor.model.Ewallet;

import java.util.UUID;

@Data
@Builder
public class EwalletResponse {

  private UUID id;
  private String name;
  private String imagePath;

  public static EwalletResponse from(Ewallet ewallet) {
    return EwalletResponse.builder()
        .id(ewallet.getId())
        .name(ewallet.getName())
        .imagePath(ewallet.getImagePath())
        .build();
  }

}
