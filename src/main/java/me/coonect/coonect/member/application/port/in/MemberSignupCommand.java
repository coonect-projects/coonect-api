package me.coonect.coonect.member.application.port.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Getter;
import me.coonect.coonect.common.validation.Validator;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.in.validation.Nickname;
import me.coonect.coonect.member.application.port.in.validation.Password;

@Getter
public class MemberSignupCommand {

  @Email
  @NotBlank
  private final String email;

  @Password
  private final String password;

  @Nickname
  private final String nickname;

  @Past
  @NotNull
  private final LocalDate birthday;

  public MemberSignupCommand(String email, String password, String nickname, LocalDate birthday) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.birthday = birthday;

    Validator.validate(this);
  }

  public Member toMember() {
    return Member.withoutId(email, password, nickname, birthday);
  }
}