package org.synrgy.setara.common.utils;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class GenericResponse<T> {

    private Integer status;
    private String message;
    private T data;

    public static <T> GenericResponse<T> success(HttpStatus status, String message, T data) {
        return GenericResponse.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> GenericResponse<T> error(HttpStatus status, String message) {
        return GenericResponse.<T>builder()
                .status(status.value())
                .message(message)
                .build();
    }
}
