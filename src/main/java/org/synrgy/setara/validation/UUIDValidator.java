package org.synrgy.setara.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.synrgy.setara.app.util.Regexp;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }

    return value.matches(Regexp.UUID);
  }

}
