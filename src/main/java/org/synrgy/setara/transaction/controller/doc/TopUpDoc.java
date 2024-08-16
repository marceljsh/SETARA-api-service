package org.synrgy.setara.transaction.controller.doc;

import io.swagger.v3.oas.annotations.Operation;
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
  summary = "Top up an e-wallet using phone number",
  requestBody = @RequestBody(
    required = true,
    content = @Content(
      mediaType = MediaType.APPLICATION_JSON_VALUE,
      schema = @Schema(implementation = TopUpRequest.class),
      examples = @ExampleObject(
        value = """
        {
          "mpin": "123456",
          "ewallet_id": "123e4567-e89b-12d3-a456-426614174000",
          "phone_number": "081234567890",
          "amount": 250000,
          "note": "Thx for the gift",
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
            "ewallet_name": "OVO",
            "phone_number": "081234567890",
            "name": "John Doe",
            "note": "Thx for the gift",
            "amount": 250000,
            "admin_fee": 2500
          }
        }"""
      )
    )
  )
)
public @interface TopUpDoc {
}
