package org.synrgy.setara.vendor.controller.media;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
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
        "data": [
          {
            "id": "dc36dbc4-d4dd-4a53-9b44-c0e7a78f08f6",
            "name": "Tahapan BCA"
          },
          {
            "id": "e292f250-1a15-44fb-8301-774f56fc54b8",
            "name": "Bank Mandiri"
          },
          {
            "id": "145e650f-0490-4b80-8ee5-14d4b68018fa",
            "name": "Bank BNI"
          }
        ]
      }"""
    )
  )
)
public @interface FetchAllBanks {
}
