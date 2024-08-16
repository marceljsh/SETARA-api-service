package org.synrgy.setara.vendor.controller.doc;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.*;

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
        "data": {
          "id": "dc36dbc4-d4dd-4a53-9b44-c0e7a78f08f6",
          "name": "Tahapan BCA"
        }
      }"""
    )
  )
)
public @interface GetBcaDoc {
}
