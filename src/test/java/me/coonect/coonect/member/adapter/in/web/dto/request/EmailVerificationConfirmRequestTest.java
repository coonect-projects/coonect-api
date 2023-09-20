package me.coonect.coonect.member.adapter.in.web.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import me.coonect.coonect.member.application.port.in.EmailVerificationConfirmCommand;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EmailVerificationConfirmRequestTest {

  @Test
  public void EmailVerificationConfirmCommand_로_매핑할_수_있다() throws Exception {
    // given
    EmailVerificationConfirmRequest request = new EmailVerificationConfirmRequest(
        "duk9741@gmail.com", "123456");

    // when
    EmailVerificationConfirmCommand command = request.toCommand();

    // then
    assertThat(command.getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(command.getCode()).isEqualTo("123456");

  }

}