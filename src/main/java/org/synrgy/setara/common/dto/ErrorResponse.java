package org.synrgy.setara.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
  private Integer code;
  private String message;
  private Boolean status;
  private Object data;

  public static ErrorResponse from(String message, HttpStatus status) {
    return new ErrorResponse(status.value(), message, false, null);
  }
}
