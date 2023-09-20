package me.coonect.coonect.member.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import me.coonect.coonect.common.error.exception.BusinessException;
import me.coonect.coonect.member.application.domain.exception.EmailDuplicationException;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.in.SendVerificationEmailCommand;
import me.coonect.coonect.member.application.port.out.infrastructure.EmailVerificationCodeGenerator;
import me.coonect.coonect.member.application.port.out.persistence.EmailVerificationCodeRepository;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import me.coonect.coonect.member.application.port.out.persistence.VerifiedEmailRepository;
import me.coonect.coonect.mock.FakeEmailVerificationCodeRepository;
import me.coonect.coonect.mock.FakeEmailVerificationMailSender;
import me.coonect.coonect.mock.FakeMemberRepository;
import me.coonect.coonect.mock.FakeVerifiedEmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EmailVerificationServiceTest {

  public MemberRepository memberRepository;

  public EmailVerificationCodeRepository emailVerificationCodeRepository;
  public VerifiedEmailRepository verifiedEmailRepository;
  public FakeEmailVerificationMailSender emailVerificationMailSender;

  public EmailVerificationService emailVerificationService;

  @BeforeEach
  public void init() {
    memberRepository = new FakeMemberRepository();
    emailVerificationMailSender = new FakeEmailVerificationMailSender("exception@exception.com");
    emailVerificationCodeRepository = new FakeEmailVerificationCodeRepository();
    verifiedEmailRepository = new FakeVerifiedEmailRepository();

    EmailVerificationCodeGenerator codeGenerator = () -> "123456";

    emailVerificationService = new EmailVerificationService(memberRepository,
        emailVerificationMailSender,
        codeGenerator,
        emailVerificationCodeRepository,
        verifiedEmailRepository);
  }

  @Test
  public void 이메일_인증_요청_시_가입되지_않은_이메일이면_인증_번호를_저장하고_메일을_발송한다() throws Exception {
    // given
    String email = "duk9741@gmail.com";

    // when
    SendVerificationEmailCommand command = new SendVerificationEmailCommand(email);
    emailVerificationService.sendVerificationEmail(command);

    // then
    assertThat(emailVerificationMailSender.email).isEqualTo(email);
    assertThat(emailVerificationMailSender.code).isEqualTo("123456");
    assertThat(emailVerificationCodeRepository.has(email)).isTrue();
    assertThat(emailVerificationCodeRepository.get(email)).isEqualTo("123456");
  }

  @Test
  public void 이메일_인증_요청_시_가입된_이메일이면_예외를_던지고_인증_번호를_저장하지_않고_메일을_발송하지_않는다() throws Exception {
    // given
    String email = "duk9741@gmail.com";
    Member member = Member.withoutId(email,
        "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10));

    memberRepository.save(member);

    // when
    // then
    assertThatThrownBy(() -> {
      SendVerificationEmailCommand command = new SendVerificationEmailCommand(email);
      emailVerificationService.sendVerificationEmail(command);
    }).isInstanceOf(EmailDuplicationException.class);

    assertThat(emailVerificationMailSender.email).isNull();
    assertThat(emailVerificationMailSender.code).isNull();
    assertThat(emailVerificationCodeRepository.has(email)).isFalse();
    assertThat(emailVerificationCodeRepository.get(email)).isNull();
  }

  @Test
  public void 메일_전송_중_예외가_발생하면_인증_번호를_저장하지_않는다() throws Exception {
    // given
    String exceptionEmail = "exception@exception.com";
    // when
    // then
    assertThatThrownBy(() -> {
      SendVerificationEmailCommand command = new SendVerificationEmailCommand(exceptionEmail);
      emailVerificationService.sendVerificationEmail(command);
    }).isInstanceOf(BusinessException.class);

    assertThat(emailVerificationMailSender.email).isNull();
    assertThat(emailVerificationMailSender.code).isNull();
    assertThat(emailVerificationCodeRepository.has(exceptionEmail)).isFalse();
    assertThat(emailVerificationCodeRepository.get(exceptionEmail)).isNull();
  }
}