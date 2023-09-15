package me.coonect.coonect.member.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import me.coonect.coonect.member.application.domain.model.Member;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberJpaEntityTest {

  @Test
  public void MemberJpaEntity_를_Member_로_변환할_수_있다() throws Exception {
    // given
    MemberJpaEntity memberJpaEntity = new MemberJpaEntity(1L,
        "duk9741@gmail.com",
        "encoded_password",
        "dukcode",
        "김덕윤",
        LocalDate.of(1995, 1, 10));

    // when
    Member member = memberJpaEntity.toMember();

    // then
    assertThat(member.getId()).isEqualTo(1L);
    assertThat(member.getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(member.getEncodedPassword()).isEqualTo("encoded_password");
    assertThat(member.getNickname()).isEqualTo("dukcode");
    assertThat(member.getName()).isEqualTo("김덕윤");
    assertThat(member.getBirthday()).isEqualTo(LocalDate.of(1995, 1, 10));
  }


  @Test
  public void Member_를_MemberJpaEntity_로_변환할_수_있다() throws Exception {
    // given
    Member member = Member.withEncodedPassword(1L,
        "duk9741@gmail.com",
        "encoded_password",
        "dukcode",
        "김덕윤",
        LocalDate.of(1995, 1, 10));

    // when
    MemberJpaEntity memberJpaEntity = MemberJpaEntity.from(member);

    // then
    assertThat(memberJpaEntity.getId()).isEqualTo(1L);
    assertThat(memberJpaEntity.getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(memberJpaEntity.getEncodedPassword()).isEqualTo("encoded_password");
    assertThat(memberJpaEntity.getNickname()).isEqualTo("dukcode");
    assertThat(memberJpaEntity.getName()).isEqualTo("김덕윤");
    assertThat(memberJpaEntity.getBirthday()).isEqualTo(LocalDate.of(1995, 1, 10));
  }
}