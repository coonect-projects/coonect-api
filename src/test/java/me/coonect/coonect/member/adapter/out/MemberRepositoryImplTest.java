package me.coonect.coonect.member.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import me.coonect.coonect.member.application.domain.model.Member;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import(MemberRepositoryImpl.class)
@SqlGroup({
    @Sql(value = "/sql/member-repository-impl-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
@DataJpaTest
class MemberRepositoryImplTest {

  @Autowired
  private MemberRepositoryImpl memberRepositoryImpl;

  @Test
  public void existByEmail_을_통해_존재하는_값으로_Member_가_존재함을_알_수_있다() {
    // given
    // when
    boolean isExist = memberRepositoryImpl.existsByEmail("duk9741@gmail.com");

    // then
    assertThat(isExist).isTrue();
  }

  @Test
  public void existByEmail_을_통해_존재하지_않는_값으로_Member_가_존재하지_않음을_알_수_있다() {
    // given
    // when
    boolean isExist = memberRepositoryImpl.existsByEmail("xxxxxxxxxxxx");

    // then
    assertThat(isExist).isFalse();
  }

  @Test
  public void existByNickname_을_통해_존재하는_값으로_Member_가_존재함을_알_수_있다() {
    // given
    // when
    boolean isExist = memberRepositoryImpl.existsByNickname("dukcode");

    // then
    assertThat(isExist).isTrue();
  }

  @Test
  public void existByNickname_을_통해_존재하지_않는_값으로_Member_가_존재하지_않음을_알_수_있다() {
    // given
    // when
    boolean isExist = memberRepositoryImpl.existsByNickname("xxxxxxx");

    // then
    assertThat(isExist).isFalse();
  }

  @Test
  public void save_로_Member_를_저장할_수_있다() {
    // given
    Member member = Member.withEncodedPassword(1L,
        "xxxxxxx@gmail.com",
        "encoded_password",
        "xxxxxxx",
        LocalDate.of(1995, 1, 10));

    // when
    Member savedMember = memberRepositoryImpl.save(member);

    // then
    assertThat(savedMember.getId()).isNotNull();
    assertThat(savedMember.getEmail()).isEqualTo("xxxxxxx@gmail.com");
    assertThat(savedMember.getEncodedPassword()).isEqualTo("encoded_password");
    assertThat(savedMember.getNickname()).isEqualTo("xxxxxxx");
    assertThat(savedMember.getBirthday()).isEqualTo(LocalDate.of(1995, 1, 10));
  }

}
