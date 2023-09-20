package me.coonect.coonect.member.application.domain.service;

import jakarta.mail.MessagingException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.common.error.exception.BusinessException;
import me.coonect.coonect.common.error.exception.ErrorCode;
import me.coonect.coonect.member.application.domain.exception.EmailDuplicationException;
import me.coonect.coonect.member.application.port.in.EmailVerificationUseCase;
import me.coonect.coonect.member.application.port.in.SendVerificationEmailCommand;
import me.coonect.coonect.member.application.port.out.infrastructure.EmailVerificationCodeGenerator;
import me.coonect.coonect.member.application.port.out.infrastructure.EmailVerificationMailSender;
import me.coonect.coonect.member.application.port.out.persistence.EmailVerificationCodeRepository;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailVerificationService implements EmailVerificationUseCase {

  private final MemberRepository memberRepository;

  private final EmailVerificationMailSender mailSender;
  private final EmailVerificationCodeGenerator codeGenerator;

  private final EmailVerificationCodeRepository emailVerificationCodeRepository;
  @Value("${coonect.email.verification.expire-minute}")
  private int emailVerificationExpireMinute;

  @Override
  public void sendVerificationEmail(SendVerificationEmailCommand command) {
    checkEmailDuplicated(command.getEmail());

    String code = codeGenerator.generate();
    emailVerificationCodeRepository.save(command.getEmail(), code,
        Duration.ofMinutes(emailVerificationExpireMinute));

    try {
      mailSender.send(command.getEmail(), code);
    } catch (MessagingException e) {
      throw new BusinessException("mail delivery error", ErrorCode.MAIL_DELIVERY_ERROR);
    }
  }

  private void checkEmailDuplicated(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new EmailDuplicationException(email);
    }
  }
}
