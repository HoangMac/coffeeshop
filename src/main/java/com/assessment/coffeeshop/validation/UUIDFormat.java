package com.assessment.coffeeshop.validation;

import com.assessment.coffeeshop.validation.validator.UUIDFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {UUIDFormatValidator.class})
public @interface UUIDFormat {
  Class<?>[] groups() default {};

  String message() default "";

  Class<? extends Payload>[] payload() default {};
}