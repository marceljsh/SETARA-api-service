package org.synrgy.setara.vendor.controller.doc;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(
  description = "Unique identifier of the merchant",
  example = "25e27af8-6b74-40b8-a1a2-7f9400cbbe88",
  required = true
)
public @interface MerchantId {
}
