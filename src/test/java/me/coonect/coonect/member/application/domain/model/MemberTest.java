package me.coonect.coonect.member.application.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberTest {

  @Test
  public void rawPassword_로_Member_를_생성할_수_있다() throws Exception {
    // given
    // when
    Member member = Member.withoutId("duk9741@gmail.com",
        "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10));

    // then
    assertThat(member.getEncodedPassword()).isNotEqualTo("@12cdefghijkl");
  }

  @Test
  public void encodedPassword_로_Member_를_생성할_수_있다() throws Exception {
    // given
    // when
    Member member = Member.withEncodedPassword(1L,
        "duk9741@gmail.com",
        "encoded_password",
        "dukcode",
        LocalDate.of(1995, 1, 10));

    // then
    assertThat(member.getEncodedPassword()).isEqualTo("encoded_password");
  }

  @Test
  public void id_없이_Member_를_생성할_수_있다() throws Exception {
    // given
    // when
    Member member = Member.withoutId("duk9741@gmail.com",
        "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10));

    // then
    assertThat(member.getId()).isNull();
    assertThat(member.getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(member.getNickname()).isEqualTo("dukcode");
    assertThat(member.getBirthday()).isEqualTo(LocalDate.of(1995, 1, 10));
  }

}