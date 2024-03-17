package com.assessment.coffeeshop.validation.validator;

import com.assessment.coffeeshop.validation.UUIDFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class UUIDFormatValidator implements ConstraintValidator<UUIDFormat, String> {

  @Override
  public boolean isValid(String uuidString, ConstraintValidatorContext cx) {
    if (uuidString == null) {
      return true;
    }
    try {
      UUID.fromString(uuidString);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }
}