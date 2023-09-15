package me.coonect.coonect.member.application.domain.model;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class MemberSignup {

  private final String email;
  private final String password;
  private final String nickname;
  private final String name;
  private final LocalDate birthday;

  public MemberSignup(String email, String password, String nickname, String name,
      LocalDate birthday) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.name = name;
    this.birthday = birthday;
  }

  public Member toMember() {
    return Member.withoutId(email, password, nickname, name, birthday);
  }
}
