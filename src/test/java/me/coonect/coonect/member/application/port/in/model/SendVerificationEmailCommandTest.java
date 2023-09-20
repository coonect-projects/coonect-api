package me.coonect.coonect.member.application.port.in.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SendVerificationEmailCommandTest {

  @Test
  public void SendVerificationEmailCommand_를_생성할_수_있다() throws Exception {
    // given
    // when
    Throwable throwable = catchThrowable(() -> {
      new SendVerificationEmailCommand("duk9741@gmail.com");
    });

    // then
    assertThat(throwable).isNull();
  }

  @Test
  public void email_은_email_형식_이어야_한다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new SendVerificationEmailCommand("xxxxxxxxxxxxx")
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("email");
  }

  @Test
  public void email_은_blank_가_아니어야_한다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new SendVerificationEmailCommand("      ")
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("email");
  }

  @Test
  public void email_은_null_이_아니어야_한다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new SendVerificationEmailCommand(null)
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("email");
  }
}