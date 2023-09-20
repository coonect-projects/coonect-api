package me.coonect.coonect.member.adapter.in.web.dto.request;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.coonect.coonect.member.application.port.in.model.MemberSignupCommand;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSignupRequest {

  private String email;
  private String password;
  private String nickname;
  private LocalDate birthday;

  public MemberSignupCommand toCommand() {
    return new MemberSignupCommand(email, password, nickname, birthday);
  }

}
