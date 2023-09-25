package me.coonect.coonect.member.application.port.in.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EmailVerificationConfirmCommandTest {

  @Test
  public void EmailVerificationConfirmCommand_를_생성할_수_있다() throws Exception {
    // given
    // when
    Throwable throwable = catchThrowable(() -> {
      new EmailVerificationConfirmCommand("duk9741@gmail.com", "123456");
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
        new EmailVerificationConfirmCommand("xxxxxxxxxxxxx", "123456")
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("email");
  }

  @Test
  public void email_은_blank_가_아니어야_한다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new EmailVerificationConfirmCommand("      ", "123456")
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("email");
  }

  @Test
  public void email_은_null_이_아니어야_한다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new EmailVerificationConfirmCommand(null, "123456")
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("email");
  }

  @Test
  public void code_는_blank_가_아니어야_한다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new EmailVerificationConfirmCommand("duk9741@gmail.com", "     ")
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("code");
  }

  @Test
  public void code_는_null_이_아니어야_한다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new EmailVerificationConfirmCommand("duk9741@gmail.com", null)
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("code");
  }

  @Test
  public void code_는_6자리_숫자여야_한다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new EmailVerificationConfirmCommand("duk9741@gmail.com", "123A")
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("code");
  }

  @Test
  public void code_는_6자리를_넘을_수_없다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new EmailVerificationConfirmCommand("duk9741@gmail.com", "1234567")
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("code");
  }
}