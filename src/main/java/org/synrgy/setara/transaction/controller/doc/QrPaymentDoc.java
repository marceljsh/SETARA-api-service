package org.synrgy.setara.transaction.controller.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.synrgy.setara.transaction.dto.QRPaymentRequest;

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
      schema = @Schema(implementation = QRPaymentRequest.class),
      examples = @ExampleObject(
        value = """
        {
          "merchant_id": "5f4f3b3b-4b3b-4b3b-4b3b-4b3b4b3b4b3b",
          "amount": 39000,
          "note": "<some-note>",
          "mpin": "667723"
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
            "merchant_name": "Los Pollos Hermanos",
            "transaction_time": "2020-12-31T23:59:59Z",
            "amount": 35000,
            "note": "<some-note>"
          }
        }"""
      )
    )
  )
)
public @interface QrPaymentDoc {
}
