package org.synrgy.setara.auth.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.app.util.Regexp;

@Data
@Builder
public class LoginRequest {

  @Size(
    min = 3,
    max = 32,
    message = "User ID must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = Regexp.SIGNATURE,
    message = "User ID can only have letters and numbers"
  )
  private String signature;

  @Size(
    min = 8,
    max = 64,
    message = "Password must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = Regexp.PASSWORD,
    message = "Password can only have letters, numbers, and special characters"
  )
  private String password;

}
