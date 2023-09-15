package me.coonect.coonect.common.validation;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;

public class Validator {

  // Your IDE may complain that the ValidatorFactory needs to be closed, but if we do that here,
  // we break the contract of ValidatorFactory#close.
  private final static jakarta.validation.Validator validator =
      buildDefaultValidatorFactory().getValidator();

  /**
   * Evaluates all Bean Validation annotations on the subject.
   */
  public static <T> void validate(T subject) {
    Set<ConstraintViolation<T>> violations = validator.validate(subject);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
