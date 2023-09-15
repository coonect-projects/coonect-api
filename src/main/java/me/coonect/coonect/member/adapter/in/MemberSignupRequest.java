package me.coonect.coonect.member.adapter.in;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.coonect.coonect.member.application.domain.model.MemberSignup;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSignupRequest {

  private String email;
  private String password;
  private String nickname;
  private String name;
  private LocalDate birthday;

  public MemberSignup toEntity() {
    return new MemberSignup(email, password, nickname, name, birthday);
  }

}
