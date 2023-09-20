package me.coonect.coonect.member.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.LocalDate;
import me.coonect.coonect.common.error.exception.NotFoundException;
import me.coonect.coonect.member.application.domain.exception.EmailDuplicationException;
import me.coonect.coonect.member.application.domain.exception.NicknameDuplicationException;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.in.model.MemberSignupCommand;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import me.coonect.coonect.member.application.port.out.persistence.VerifiedEmailRepository;
import me.coonect.coonect.mock.FakeMemberRepository;
import me.coonect.coonect.mock.FakeVerifiedEmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberSignupServiceTest {

  private MemberRepository memberRepository;
  private VerifiedEmailRepository verifiedEmailRepository;
  private MemberSignupService memberSignupService;

  @BeforeEach
  public void init() {
    memberRepository = new FakeMemberRepository();
    verifiedEmailRepository = new FakeVerifiedEmailRepository();
    memberSignupService = new MemberSignupService(memberRepository, verifiedEmailRepository);
  }

  @Test
  public void 회원가입이_가능하다() throws Exception {
    // given
    verifiedEmailRepository.save("duk9741@gmail.com", "123456", Duration.ZERO);

    MemberSignupCommand memberSignupCommand = new MemberSignupCommand("duk9741@gmail.com",
        "123456", "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10));

    // when
    Member member = memberSignupService.signup(memberSignupCommand);

    // then
    assertThat(member.getId()).isEqualTo(1L);
    assertThat(member.getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(member.getNickname()).isEqualTo("dukcode");
    assertThat(member.getEncodedPassword()).isNotEqualTo("@12cdefghijkl");
    assertThat(member.getBirthday()).isEqualTo(LocalDate.of(1995, 1, 10));
  }

  @Test
  public void 인증된_코드와_요청_코드가_다르면_회원가입이_불가능하다() throws Exception {
    // given
    String code = "123456";
    String differentCode = "000000";
    verifiedEmailRepository.save("duk9741@gmail.com", code, Duration.ZERO);

    MemberSignupCommand memberSignupCommand = new MemberSignupCommand("duk9741@gmail.com",
        differentCode, "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10));

    // when
    // then
    assertThatThrownBy(() -> {
      memberSignupService.signup(memberSignupCommand);
    }).isInstanceOf(NotFoundException.class);

  }

  @Test
  public void 중복된_이메일로_회원가입이_불가능하다() throws Exception {
    // given
    memberRepository.save(Member.withoutId("duk9741@gmail.com",
        "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10)));

    MemberSignupCommand memberSignupCommand = new MemberSignupCommand("duk9741@gmail.com",
        "123456", "@12cdefghijkl",
        "nickname",
        LocalDate.of(1995, 1, 10));

    // when
    // then
    assertThatThrownBy(() -> memberSignupService.signup(memberSignupCommand))
        .isInstanceOf(EmailDuplicationException.class);
  }

  @Test
  public void 중복된_닉네임으로_회원가입이_불가능하다() throws Exception {
    // given
    memberRepository.save(Member.withoutId("duk9741@gmail.com",
        "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10)));

    MemberSignupCommand memberSignupCommand = new MemberSignupCommand("xxxxxxxx@gmail.com",
        "123456", "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10));

    // when
    // then
    assertThatThrownBy(() -> memberSignupService.signup(memberSignupCommand))
        .isInstanceOf(NicknameDuplicationException.class);
  }

  @Test
  public void 회원_가입_후_저장된_인증_번호는_삭제된다() throws Exception {
    // given
    String code = "123456";
    String email = "dukcode@gmail.com";

    verifiedEmailRepository.save(email, code, Duration.ZERO);

    MemberSignupCommand command = new MemberSignupCommand(email,
        code, "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10));

    // when
    Member member = memberSignupService.signup(command);

    // then
    assertThat(member.getId()).isNotNull();
    assertThat(verifiedEmailRepository.has(email)).isFalse();
  }

  @Test
  public void 두_사용자가_같은_인증_번호를_받고_인증_후_동시에_회원_가입_할_수_있다() throws Exception {
    // given
    String code = "123456";
    verifiedEmailRepository.save("dukcode1@gmail.com", code, Duration.ZERO);
    verifiedEmailRepository.save("dukcode2@gmail.com", code, Duration.ZERO);

    MemberSignupCommand command1 = new MemberSignupCommand("dukcode1@gmail.com",
        code, "@12cdefghijkl",
        "dukcode1",
        LocalDate.of(1995, 1, 10));
    MemberSignupCommand command2 = new MemberSignupCommand("dukcode2@gmail.com",
        code, "@12cdefghijkl",
        "dukcode2",
        LocalDate.of(1995, 1, 10));

    // when
    Member member1 = memberSignupService.signup(command1);
    Member member2 = memberSignupService.signup(command2);

    // then
    assertThat(member1.getId()).isNotNull();
    assertThat(member2.getId()).isNotNull();
  }
}