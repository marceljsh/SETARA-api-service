package org.synrgy.setara.auth.controller.media;

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
        "data": {
          "user": {
            "name": "Andhika Putra",
            "account_number": "2891376451",
            "balance": 15000000,
            "image_path": "/setara-api-service/images/user/Andhika-Putra.png"
          },
          "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnb2QiLCJpYXQiOjE3MjAxOTQzNzYsImV4cCI6MTcyMDE5Nzk3Nn0.SXkMYJXPrpGfnz8FX4n3I0IUeKlQdLjvXZUSypAe1Ig"
        }
      }"""
    )
  )
)
public @interface AuthRes {
}
