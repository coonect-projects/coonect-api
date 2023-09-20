package me.coonect.coonect.mock;

import lombok.Builder;
import me.coonect.coonect.member.adapter.in.web.EmailVerificationController;
import me.coonect.coonect.member.adapter.in.web.MemberSignupController;
import me.coonect.coonect.member.adapter.in.web.NicknameValidationController;
import me.coonect.coonect.member.application.domain.service.EmailVerificationService;
import me.coonect.coonect.member.application.domain.service.MemberSignupService;
import me.coonect.coonect.member.application.domain.service.NicknameValidationService;
import me.coonect.coonect.member.application.port.in.EmailVerificationUseCase;
import me.coonect.coonect.member.application.port.in.MemberSignupUseCase;
import me.coonect.coonect.member.application.port.in.NicknameValidationUseCase;
import me.coonect.coonect.member.application.port.out.infrastructure.EmailVerificationCodeGenerator;
import me.coonect.coonect.member.application.port.out.infrastructure.EmailVerificationMailSender;
import me.coonect.coonect.member.application.port.out.persistence.EmailVerificationCodeRepository;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import me.coonect.coonect.member.application.port.out.persistence.VerifiedEmailRepository;

public class TestContainer {

  public MemberRepository memberRepository;

  public EmailVerificationCodeRepository emailVerificationCodeRepository;
  public VerifiedEmailRepository verifiedEmailRepository;
  public EmailVerificationMailSender emailVerificationMailSender;

  public MemberSignupUseCase memberSignupUseCase;
  public NicknameValidationUseCase nicknameValidationUseCase;
  public EmailVerificationUseCase emailVerificationUseCase;


  public MemberSignupController memberSignupController;
  public NicknameValidationController nicknameValidationController;
  public EmailVerificationController emailVerificationController;

  @Builder
  public TestContainer(EmailVerificationCodeGenerator codeGenerator, String exceptionEmail) {
    memberRepository = new FakeMemberRepository();

    emailVerificationMailSender = new FakeEmailVerificationMailSender(exceptionEmail);
    emailVerificationCodeRepository = new FakeEmailVerificationCodeRepository();
    verifiedEmailRepository = new FakeVerifiedEmailRepository();

    memberSignupUseCase = new MemberSignupService(memberRepository);
    nicknameValidationUseCase = new NicknameValidationService(memberRepository);
    emailVerificationUseCase = new EmailVerificationService(memberRepository,
        emailVerificationMailSender,
        codeGenerator,
        emailVerificationCodeRepository,
        verifiedEmailRepository);

    memberSignupController = new MemberSignupController(memberSignupUseCase);
    nicknameValidationController = new NicknameValidationController(nicknameValidationUseCase);
    emailVerificationController = new EmailVerificationController(emailVerificationUseCase);
  }
}
