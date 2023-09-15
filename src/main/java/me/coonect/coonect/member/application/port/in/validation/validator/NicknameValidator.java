package me.coonect.coonect.member.application.port.in.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.coonect.coonect.member.application.port.in.validation.Nickname;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {

  private static final String REGEX = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,10}$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value.matches(REGEX);
  }
}
