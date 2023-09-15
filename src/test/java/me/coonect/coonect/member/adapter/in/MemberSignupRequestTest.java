package me.coonect.coonect.member.adapter.in;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import me.coonect.coonect.member.application.port.in.MemberSignupCommand;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberSignupRequestTest {

  @Test
  public void MemberSignupRequest_는_MemberSignupCommand_로_변환될_수_있다() throws Exception {
    // given
    MemberSignupRequest request = new MemberSignupRequest("duk9741@gmail.com",
        "!@#qwe123",
        "닉네임",
        LocalDate.of(1995, 1, 10));

    // when
    MemberSignupCommand command = request.toCommand();

    // then
    assertThat(command.getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(command.getPassword()).isEqualTo("!@#qwe123");
    assertThat(command.getNickname()).isEqualTo("닉네임");
    assertThat(command.getBirthday()).isEqualTo(LocalDate.of(1995, 1, 10));

  }

}