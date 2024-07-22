package org.synrgy.setara.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {
    private Integer code;
    private String message;
    private Boolean status;
    private T data;

    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(200, message, true, data);
    }

    public static <T> BaseResponse<T> failure(Integer code, String message) {
        return new BaseResponse<>(code, message, false, null);
    }
}
