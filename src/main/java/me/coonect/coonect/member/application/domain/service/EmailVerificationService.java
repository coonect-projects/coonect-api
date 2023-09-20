package me.coonect.coonect.member.application.domain.service;

import jakarta.mail.MessagingException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.common.error.exception.BusinessException;
import me.coonect.coonect.common.error.exception.ErrorCode;
import me.coonect.coonect.member.application.domain.exception.EmailDuplicationException;
import me.coonect.coonect.member.application.port.in.EmailVerificationConfirmCommand;
import me.coonect.coonect.member.application.port.in.EmailVerificationUseCase;
import me.coonect.coonect.member.application.port.in.SendVerificationEmailCommand;
import me.coonect.coonect.member.application.port.out.infrastructure.EmailVerificationCodeGenerator;
import me.coonect.coonect.member.application.port.out.infrastructure.EmailVerificationMailSender;
import me.coonect.coonect.member.application.port.out.persistence.EmailVerificationCodeRepository;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import me.coonect.coonect.member.application.port.out.persistence.VerifiedEmailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailVerificationService implements EmailVerificationUseCase {

  private final MemberRepository memberRepository;

  private final EmailVerificationMailSender mailSender;
  private final EmailVerificationCodeGenerator codeGenerator;

  private final EmailVerificationCodeRepository emailVerificationCodeRepository;
  private final VerifiedEmailRepository verifiedEmailRepository;
  @Value("${coonect.email.verification.expire-minute}")
  private int emailVerificationExpireMinute;

  @Override
  public void sendVerificationEmail(SendVerificationEmailCommand command) {
    String email = command.getEmail();
    checkEmailDuplicated(email);

    String code = codeGenerator.generate();
    emailVerificationCodeRepository.save(email, code,
        Duration.ofMinutes(emailVerificationExpireMinute));

    try {
      mailSender.send(email, code);
    } catch (MessagingException e) {
      emailVerificationCodeRepository.remove(email);
      throw new BusinessException("mail delivery error", ErrorCode.MAIL_DELIVERY_ERROR);
    }
  }

  @Override
  public boolean verifyEmail(EmailVerificationConfirmCommand query) {
    String email = query.getEmail();
    String code = query.getCode();

    checkEmailDuplicated(email);

    if (isVerify(email, code)) {
      verifiedEmailRepository.save(code, email);
      emailVerificationCodeRepository.remove(email);
      return true;
    }

    return false;
  }

  private boolean isVerify(String email, String code) {
    return emailVerificationCodeRepository.has(email) &&
        emailVerificationCodeRepository.get(email).equals(code);
  }

  private void checkEmailDuplicated(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new EmailDuplicationException(email);
    }
  }
}
