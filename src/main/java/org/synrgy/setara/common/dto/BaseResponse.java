package org.synrgy.setara.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse<T> {

  @Schema(description = "Indicates if the request was successful")
  private boolean success;

  @Schema(description = "Response message")
  private String message;

  @Schema(description = "Response data")
  private T data;

  public static <T> BaseResponse<T> success(String message, T data) {
    return BaseResponse.<T>builder()
        .success(true)
        .message(message)
        .data(data)
        .build();
  }

  public static <T> BaseResponse<T> fail(String message) {
    return BaseResponse.<T>builder()
        .success(false)
        .message(message)
        .data(null)
        .build();
  }

}
