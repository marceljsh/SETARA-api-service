package org.synrgy.setara.contact.controller.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.synrgy.setara.transaction.dto.TopUpRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
  summary = "Toggle favorite status of an e-wallet contact",
  requestBody = @RequestBody(
    required = true,
    content = @Content(
      mediaType = MediaType.APPLICATION_JSON_VALUE,
      schema = @Schema(implementation = TopUpRequest.class),
      examples = @ExampleObject(
        value = """
        {
          "favorite": "true"
        }"""
      )
    )
  ),
  responses = @ApiResponse(
    responseCode = "200",
    description = "Successful operation",
    content = @Content(
      mediaType = MediaType.APPLICATION_JSON_VALUE,
      examples = @ExampleObject(
        value = """
        {
          "success": true,
          "message": "OK",
          "data": null
        }"""
      )
    )
  )
)
@Parameter(
  name = "id",
  description = "ID of the e-wallet contact",
  required = true,
  example = "123e4567-e89b-12d3-a456-426614174000"
)
public @interface ToggleFavoriteEwalletContactDoc {
}
