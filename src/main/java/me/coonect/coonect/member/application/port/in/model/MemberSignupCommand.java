package me.coonect.coonect.member.application.port.in.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Getter;
import me.coonect.coonect.common.validation.Validator;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.in.model.validation.EmailVerificationCode;
import me.coonect.coonect.member.application.port.in.model.validation.Nickname;
import me.coonect.coonect.member.application.port.in.model.validation.Password;

@Getter
public class MemberSignupCommand {

  @Email
  @NotBlank(message = "email은 공백일 수 없습니다.")
  private final String email;

  @EmailVerificationCode
  private final String emailVerificationCode;

  @Password
  private final String password;

  @Nickname
  private final String nickname;

  @Past(message = "birthday는 과거여야 합니다.")
  @NotNull(message = "birthday는 null일 수 없습니다.")
  private final LocalDate birthday;

  public MemberSignupCommand(String email, String emailVerificationCode, String password,
      String nickname, LocalDate birthday) {
    this.email = email;
    this.emailVerificationCode = emailVerificationCode;
    this.password = password;
    this.nickname = nickname;
    this.birthday = birthday;

    Validator.validate(this);
  }

  public Member toMember() {
    return Member.withoutId(email, password, nickname, birthday);
  }
}
