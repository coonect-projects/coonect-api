package me.coonect.coonect.member.application.port.in;

import lombok.Getter;
import me.coonect.coonect.common.validation.Validator;
import me.coonect.coonect.member.application.port.in.validation.Nickname;

@Getter
public class NicknameValidationQuery {

  @Nickname
  private final String nickname;

  private NicknameValidationQuery(String nickname) {
    this.nickname = nickname;

    Validator.validate(this);
  }

  public static NicknameValidationQuery of(String nickname) {
    return new NicknameValidationQuery(nickname);
  }
}
