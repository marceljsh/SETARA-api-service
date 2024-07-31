package org.synrgy.setara.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

  private boolean success;

  private String message;

  private T data;

  public static <T> ApiResponse<T> success(String message, T data) {
    return ApiResponse.<T>builder()
        .success(true)
        .message(message)
        .data(data)
        .build();
  }

  public static <T> ApiResponse<T> fail(String message) {
    return ApiResponse.<T>builder()
        .success(false)
        .message(message)
        .data(null)
        .build();
  }

}
