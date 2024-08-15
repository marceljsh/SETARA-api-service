package org.synrgy.setara.auth.controller.media;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.MediaType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@RequestBody(
  description = "Login credentials",
  required = true,
  content = @Content(
    mediaType = MediaType.APPLICATION_JSON_VALUE,
    examples = @ExampleObject(
      name = "Login Request",
      summary = "A sample login request",
      value = "{ \"signature\": \"ADTP604T\", \"password\": \"andika12345\" }"
    )
  )
)
public @interface AuthReq {
}
