package org.synrgy.setara.vendor.controller.doc;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
          "data": [
            {
              "id": "867e89f5-99f9-43cf-a70c-7b1c90f9510d",
              "name": "Los Pollos Hermanos",
              "terminal_id": "YLC",
              "nmid": "ID9668300169805",
              "address": "12100 Coors Rd SW, Albuquerque",
              "qris_code": "iVBORw0KGgoAAAANSUhEUgAAAZAAAAGQAQAAAACoxAthAAACAklEQVR4Xu2YQXbEIAxDySrHyFHhqBwjq6FIdjJJZ9o+L8eVFgQbfVZ6wEsZYZXvjb8lJCohUQmJSkhUQqISEpWQqIREJQR6FNf6KLXPoWz7MkafnYqBqkKyIlb3dYzG9e1cQO9iEXKrkyC9IBzISjHPQGCQGusJ+S8Iy2VGhzMh/wipY8cMpc2E5Ef45cXArGyXbW4WIbc6B+JCLpiay3CsCUmK3EQjJq3M6LxIyIs+HGFg/FFoCdlmt207Zz9cFkKSIFO98IrAbKUHU/4nQAm4mlGIKw2CNOzIySnEpHZw3RqLOYXkRAZyMXBFwMgeQwR3499CIVkROx/6SoTu4jtw8G2EZEXo2XEnYGBWGBNsM9gTkhNxruJZMBG4B7KycxuWPFGE5ESarWDA7TB6QWq4A/UmMEKSIH5cHJ5ndGh8WGpcQrIhU6fbHw22A5I0q8YQmYS40iBISMFJsToyPY7wpHgTGCFpkIHA8APE4DMmQFC6hLgSIW0zty/T3Y6G9YSkRQ7h4DiXnxcINqzWFHLX5yPXrBxujDtvjNNNCYFyIdaEsTm9oPwtMELyIL34y7DRPXa+F16TJCQxgnDAvfGkIIzAAK5C8iOXganZTouQtAi/p5up4UJhdAC7hORDXESOQ2Iaj56QxEhEQqISEpWQqIREJSQqIVEJiUpIVEKi+gJfzIbnXo5XkwAAAABJRU5ErkJggg==",
              "image_path": "/setara-api-service/images/merchant/qrcode_Los-Pollos-Hermanos.png"
            }
          ]
        }"""
      )
    )
  )
})
public @interface FetchSingleMerchant {
}
