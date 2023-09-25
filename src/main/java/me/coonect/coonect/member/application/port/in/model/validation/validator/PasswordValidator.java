package me.coonect.coonect.member.application.port.in.model.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import me.coonect.coonect.member.application.port.in.model.validation.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

  private static final String REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !Objects.isNull(value) && value.matches(REGEX);
  }
}
