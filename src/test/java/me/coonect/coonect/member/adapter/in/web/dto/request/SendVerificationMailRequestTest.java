package me.coonect.coonect.member.adapter.in.web.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import me.coonect.coonect.member.application.port.in.SendVerificationEmailCommand;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SendVerificationMailRequestTest {

  @Test
  public void SendVerificationEmailCommand_로_매핑할_수_있다() throws Exception {
    // given
    SendVerificationMailRequest request = new SendVerificationMailRequest(
        "duk9741@gmail.com");

    // when
    SendVerificationEmailCommand command = request.toCommand();

    // then
    assertThat(command.getEmail()).isEqualTo("duk9741@gmail.com");

  }

}