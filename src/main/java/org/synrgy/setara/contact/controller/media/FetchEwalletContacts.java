package org.synrgy.setara.contact.controller.media;

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
            "id": "c5bd74b7-8b9d-4e25-b825-d56275b59312",
            "ewallet": {
              "id": "f0a62a14-636c-4a88-aa29-5fd30d30272d",
              "name": "OVO",
              "image_path": "/setara-api-service/images/ewallet/OVO.png"
            },
            "name": "Andre Benjamin",
            "phone_number": "081398765432",
            "image_path": "/setara-api-service/images/ewallet-user/AndreBenjamin.jpg"
            "favorite": false
          }
        ]
      }"""
    )
  )
)
public @interface FetchEwalletContacts {
}
