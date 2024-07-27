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
public class BaseResponse<T> {
    private Integer code;
    private String message;
    private Boolean status;
    private T data;

    public static <T> BaseResponse<T> success(HttpStatus status, T data, String message) {
        return new BaseResponse<>(status.value(), message, true, data);
    }

    public static <T> BaseResponse<T> failure(HttpStatus status, String message) {
        return new BaseResponse<>(status.value(), message, false, null);
    }
}
