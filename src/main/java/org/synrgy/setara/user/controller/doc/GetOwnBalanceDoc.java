package org.synrgy.setara.user.controller.doc;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
  responseCode = "200",
  description = "Successful operation",
  content = @Content(
    mediaType = MediaType.APPLICATION_JSON_VALUE,
    examples = @ExampleObject(
      value = """
      {
        "success": true,
        "message": "OK",
        "data": {
          "checkTime": "2024-08-15T06:02:00.228Z",
          "balance": 15000000
        }
      }"""
    )
  )
)
public @interface GetOwnBalanceDoc {
}
