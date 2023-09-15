package me.coonect.coonect.member.application.port.in.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.coonect.coonect.member.application.port.in.validation.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

  private static final String REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value.matches(REGEX);
  }
}
