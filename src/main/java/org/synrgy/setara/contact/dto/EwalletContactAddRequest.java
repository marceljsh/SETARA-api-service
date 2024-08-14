package org.synrgy.setara.contact.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.app.util.Regexp;

import java.util.UUID;

@Data
@Builder
public class EwalletContactAddRequest {

  @Size(
    min = 3,
    max = 64,
    message = "Name must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = Regexp.DISPLAY_NAME,
    message = "Name can only have letters, numbers, and spaces"
  )
  private String name;

  @org.hibernate.validator.constraints.UUID(
    message = "Ewallet User ID must be a valid UUID"
  )
  private UUID ewalletUserId;

  private boolean favorite;

}
