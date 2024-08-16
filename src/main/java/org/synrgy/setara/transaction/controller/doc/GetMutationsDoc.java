package org.synrgy.setara.transaction.controller.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
  summary = "Top up an e-wallet using phone number",
  requestBody = @RequestBody(
    required = true,
    content = @Content(
      mediaType = MediaType.APPLICATION_JSON_VALUE,
      examples = @ExampleObject(
        value = """
        {
          "start_date": "2024-05-01",
          "end_date": "2024-05-31",
          "mutation_type": "ALL"
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
          "data": {
            "data": [
              {
                "reference_number": "<ref-no>",
                "unique_code": "<unique-code>",
                "transaction_id": "5f4f3b3b-4b3b-4b3b-4b3b-4b3b4b3b4b3b",
                "transaction_type": "Transfer",
                "total_amount": 35000,
                "transaction_time": "2020-12-31T23:59:59Z",
                "destination": "1234567890"
              }
            ],
            "current_page": 0,
            "total_pages": 1,
            "size": 10,
            "total": 1
          }
        }"""
      )
    )
  )
)
@Parameter(
  name = "page",
  description = "Page number",
  required = false,
  schema = @Schema(
    type = "integer",
    defaultValue = "false"
  ),
  example = "0"
)
@Parameter(
  name = "page",
  description = "Page size",
  required = false,
  schema = @Schema(
    type = "integer",
    defaultValue = "false"
  ),
  example = "10"
)
public @interface GetMutationsDoc {
}
