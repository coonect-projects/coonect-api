package me.coonect.coonect.member.adapter.in.web.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import me.coonect.coonect.member.application.domain.model.Member;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberResponseTest {

  @Test
  public void Member_는_MemberResponse_로_변환할_수_있다() throws Exception {
    // given
    Member member = Member.withEncodedPassword(1L,
        "duk9741@gmail.com",
        "encoded_password",
        "dukcode",
        LocalDate.of(1995, 1, 10));

    // when
    MemberResponse response = MemberResponse.from(member);

    // then
    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(response.getNickname()).isEqualTo("dukcode");
  }

}