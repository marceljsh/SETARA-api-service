package org.synrgy.setara.transaction.controller.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.synrgy.setara.transaction.dto.TransferRequest;

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
      schema = @Schema(implementation = TransferRequest.class),
      examples = @ExampleObject(
        value = """
        {
          "dest_bank_id": "123e4567-e89b-12d3-a456-426614174000",
          "dest_account_number": "1234567890",
          "amount": 250000,
          "mpin": "123456",
          "note": "For da goods",
          "name": "John Doe",
          "save_contact": true
        }"""
      )
    )
  ),
  responses = @ApiResponse(
    responseCode = "201",
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
            "transaction_time": "2020-01-01T00:00:00Z",
            "bank_name": "Tahapan BCA",
            "account_number": "1234567890",
            "amount": 250000,
            "admin_fee": 2500,
            "note": "For da goods"
          }
        }"""
      )
    )
  )
)
public @interface TransferDoc {
}
