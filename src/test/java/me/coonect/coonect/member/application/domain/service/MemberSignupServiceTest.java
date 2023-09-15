package me.coonect.coonect.member.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import me.coonect.coonect.member.application.domain.exception.EmailDuplicationException;
import me.coonect.coonect.member.application.domain.exception.NicknameDuplicationException;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.domain.model.MemberSignup;
import me.coonect.coonect.member.application.port.out.MemberRepository;
import me.coonect.coonect.mock.FakeMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberSignupServiceTest {

  private MemberRepository memberRepository;
  private MemberSignupService memberSignupService;

  @BeforeEach
  public void init() {
    memberRepository = new FakeMemberRepository();
    memberSignupService = new MemberSignupService(memberRepository);
  }

  @Test
  public void 회원가입이_가능하다() throws Exception {
    // given
    MemberSignup memberSignup = new MemberSignup("duk9741@gmail.com",
        "raw_password",
        "dukcode",
        "김덕윤",
        LocalDate.of(1995, 1, 10));

    // when
    Member member = memberSignupService.signup(memberSignup);

    // then
    assertThat(member.getId()).isEqualTo(1L);
    assertThat(member.getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(member.getNickname()).isEqualTo("dukcode");
    assertThat(member.getName()).isEqualTo("김덕윤");
    assertThat(member.getEncodedPassword()).isNotEqualTo("raw_password");
    assertThat(member.getBirthday()).isEqualTo(LocalDate.of(1995, 1, 10));
  }

  @Test
  public void 중복된_이메일로_회원가입이_불가능하다() throws Exception {
    // given
    memberRepository.save(Member.withoutId("duk9741@gmail.com",
        "raw_password",
        "dukcode",
        "김덕윤",
        LocalDate.of(1995, 1, 10)));

    MemberSignup memberSignup = new MemberSignup("duk9741@gmail.com",
        "raw_password",
        "nickname",
        "김덕윤",
        LocalDate.of(1995, 1, 10));

    // when
    // then
    assertThatThrownBy(() -> memberSignupService.signup(memberSignup))
        .isInstanceOf(EmailDuplicationException.class);
  }

  @Test
  public void 중복된_닉네임으로_회원가입이_불가능하다() throws Exception {
    // given
    memberRepository.save(Member.withoutId("duk9741@gmail.com",
        "raw_password",
        "dukcode",
        "김덕윤",
        LocalDate.of(1995, 1, 10)));

    MemberSignup memberSignup = new MemberSignup("xxxxxxxx@gmail.com",
        "raw_password",
        "dukcode",
        "김덕윤",
        LocalDate.of(1995, 1, 10));

    // when
    // then
    assertThatThrownBy(() -> memberSignupService.signup(memberSignup))
        .isInstanceOf(NicknameDuplicationException.class);
  }
}