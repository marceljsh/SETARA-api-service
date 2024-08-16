package org.synrgy.setara.contact.controller.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
  summary = "Get all bank contacts of current user",
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
          "data": [
            {
              "id": "c5bd74b7-8b9d-4e25-b825-d56275b59312",
              "bank": {
                "id": "91393105-b623-4d80-85e3-0824e1f26c0a",
                "name": "Tahapan BCA"
              },
              "name": "Putra Wijaya",
              "account_number": "2946145032",
              "image_path": "/setara-api-service/images/bank-contacts/Putra-Wijaya.png",
              "favorite": false
            }
          ]
        }"""
      )
    )
  )
)
@Parameter(
  name = "fav-only",
  description = "Filter by favorite status",
  required = false,
  schema = @Schema(
    type = "boolean",
    defaultValue = "false"
  ),
  example = "true"
)
public @interface GetOwnBankContactsDoc {
}
