package me.coonect.coonect.member.application.port.in.model.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.coonect.coonect.member.application.port.in.model.validation.EmailVerificationCode;

public class EmailVerificationCodeValidator implements
    ConstraintValidator<EmailVerificationCode, String> {

  private static final String REGEX = "^[0-9]{6}$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value.matches(REGEX);
  }
}
