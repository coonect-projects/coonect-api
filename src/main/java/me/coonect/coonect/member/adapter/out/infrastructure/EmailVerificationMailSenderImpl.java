package me.coonect.coonect.member.adapter.out.infrastructure;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.port.out.infrastructure.EmailVerificationMailSender;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailVerificationMailSenderImpl implements EmailVerificationMailSender {

  private static final String SUBJECT = "[쿠넥트] 회원가입을 위해 메일을 인증해 주세요.";

  private final JavaMailSender javaMailSender;
  private final EmailVerificationHtmlLoader htmlLoader;

  @Override
  public void send(String email, String code) throws MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    helper.setTo(email);
    helper.setSubject(SUBJECT);
    helper.setText(htmlLoader.loadWith(code), true);
    helper.addInline("coonect-logo", new ClassPathResource("templates/images/coonect-logo.png"));
    helper.addInline("mail-icon", new ClassPathResource("templates/images/mail-icon.png"));
    javaMailSender.send(mimeMessage);
  }
}
