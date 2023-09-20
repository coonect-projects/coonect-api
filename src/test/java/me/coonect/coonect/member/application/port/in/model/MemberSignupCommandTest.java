package me.coonect.coonect.member.application.port.in.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import me.coonect.coonect.member.application.domain.model.Member;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberSignupCommandTest {

  @Test
  public void MemberSignupCommand_를_생성할_수_있다() throws Exception {
    // given
    // when
    Throwable throwable = catchThrowable(() -> {
      new MemberSignupCommand("duk9741@gmail.com",
          "123456", "@12cdefghijkl",
          "dukcode",
          LocalDate.of(1995, 1, 10));
    });

    // then
    assertThat(throwable).isNull();
  }

  @Test
  public void email_은_email_형식_이어야_한다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("xxxxxxxxxxxxx",
            "123456", "1a!4567890",
            "dukcode",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("email");
  }

  @Test
  public void email_은_blank_가_아니어야_한다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("      ",
            "123456", "1a!4567890",
            "dukcode",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("email");
  }

  @Test
  public void email_은_null_이_아니어야_한다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand(null,
            "123456", "1a!4567890",
            "dukcode",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("email");
  }

  @Test
  public void password_는_8글자_이상이어야_한다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "1a!4567",
            "dukcode",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("password");
  }

  @Test
  public void password_는_20글자_이하여야_한다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "1a!456789012345678901",
            "dukcode",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("password");
  }

  @Test
  public void password_는_최소_한_글자의_영문자가_포함되어야_한다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "!234567890",
            "dukcode",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("password");
  }

  @Test
  public void password_는_최소_한_글자의_숫자가_포함되어야_한다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "!bcdefghijkl",
            "dukcode",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("password");
  }

  @Test
  public void password_는_최소_한_글자의_특수문자가_포함되어야_한다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "12cdefghijkl",
            "dukcode",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("password");
  }

  @Test
  public void nickname_은_2자리_이상이어야_한다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "12cdefghijkl",
            "d",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("nickname");
  }

  @Test
  public void nickname_은_10자리_이하여야_한다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "12cdefghijkl",
            "12345678901",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("nickname");
  }

  @Test
  public void nickname_은_빈칸이_존재하면_안된다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "a   a",
            "12345678901",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("nickname");
  }

  @Test
  public void nickname_은_자음만_존재하면_안된다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "@12cdefghijkl",
            "김ㅁㅁ",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("nickname");
  }

  @Test
  public void nickname_은_모음만_존재하면_안된다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "@12cdefghijkl",
            "김ㅏㅏ",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("nickname");
  }

  @Test
  public void nickname_은_특수문자를_포함할_수_없다() throws Exception {
    // given
    // when
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "@12cdefghijkl",
            "김@@#",
            LocalDate.of(1995, 1, 10))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("nickname");
  }

  @Test
  public void birthday_는_null_일_수_없다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "@12cdefghijkl",
            "dukcode",
            null)
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("birthday");
  }

  @Test
  public void birthday_는_현재보다_과거여야_한다() throws Exception {
    // given
    // when
    // then
    assertThatThrownBy(() ->
        new MemberSignupCommand("duk9741@gmail.com",
            "123456", "@12cdefghijkl",
            "dukcode",
            LocalDate.now().plusDays(1))
    ).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("birthday");
  }

  @Test
  public void MemberSignupCommand_를_Member_로_변환할_수_있다() throws Exception {
    // given
    MemberSignupCommand memberSignupCommand

        = new MemberSignupCommand("duk9741@gmail.com",
        "123456", "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10));
    // when
    Member member = memberSignupCommand.toMember();

    // then
    assertThat(member.getId()).isNull();
    assertThat(member.getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(member.getNickname()).isEqualTo("dukcode");
    assertThat(member.getEncodedPassword()).isNotEqualTo("@12cdefghijkl");
    assertThat(member.getBirthday()).isEqualTo(LocalDate.of(1995, 1, 10));

  }
}