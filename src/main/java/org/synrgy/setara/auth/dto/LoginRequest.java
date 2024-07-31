package org.synrgy.setara.auth.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class LoginRequest {

  @Size(
    min = 3,
    max = 32,
    message = "User ID must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = "^[a-zA-Z0-9]+$",
    message = "User ID can only have letters and numbers"
  )
  @Schema(description = "User signature", example = "JANE1234")
  private String signature;

  @Size(
    min = 8,
    max = 64,
    message = "Password must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = "^[a-zA-Z0-9!@#$%^&*()_\\-+=\\[\\]{}|;:'\",.<>/?\\\\]*$",
    message = "Password can only have letters, numbers, and special characters"
  )
  @Schema(description = "User password", example = "jane123")
  private String password;

}
