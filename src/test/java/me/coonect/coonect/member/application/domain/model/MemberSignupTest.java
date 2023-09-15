package me.coonect.coonect.member.application.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberSignupTest {

  @Test
  public void MemberSignup_을_Member_로_변환할_수_있다() throws Exception {
    // given
    MemberSignup memberSignup = new MemberSignup("duk9741@gmail.com",
        "raw_password",
        "dukcode",
        "김덕윤",
        LocalDate.of(1995, 1, 10));

    // when
    Member member = memberSignup.toMember();

    // then
    assertThat(member.getId()).isNull();
    assertThat(member.getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(member.getNickname()).isEqualTo("dukcode");
    assertThat(member.getName()).isEqualTo("김덕윤");
    assertThat(member.getEncodedPassword()).isNotEqualTo("raw_password");
    assertThat(member.getBirthday()).isEqualTo(LocalDate.of(1995, 1, 10));

  }
}