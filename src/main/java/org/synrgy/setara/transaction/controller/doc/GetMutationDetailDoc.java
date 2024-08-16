package org.synrgy.setara.transaction.controller.doc;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

import java.lang.annotation.*;

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
          "data": {
            "reference_number": "<ref-no>",
            "unique_code": "<unique-code>",
            "transaction_id": "5f4f3b3b-4b3b-4b3b-4b3b-4b3b4b3b4b3b",
            "transaction_type": "Transfer",
            "total_amount": 35000,
            "transaction_time": "2020-12-31T23:59:59Z",
            "destination": "1234567890"
          }
        }"""
      )
    )
  )
})
@Parameter(
  name = "tx-id",
  description = "ID of the mutation (transaction)",
  required = true,
  example = "123e4567-e89b-12d3-a456-426614174000"
)
public @interface GetMutationDetailDoc {
}
