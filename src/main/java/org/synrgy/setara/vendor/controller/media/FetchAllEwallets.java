package org.synrgy.setara.vendor.controller.media;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses( value = {
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
              "id": "e76103b2-df7d-496d-a3ab-e38bd8a3a294",
              "name": "OVO",
              "image_path": "/setara-api-service/images/ewallet/OVO.png"
            },
            {
              "id": "d162a822-c021-4206-b17f-8040710b5efd",
              "name": "Dana",
              "image_path": "/setara-api-service/images/ewallet/Dana.png"
            }
          ]
        }"""
      )
    )
  )
})
public @interface FetchAllEwallets {
}
