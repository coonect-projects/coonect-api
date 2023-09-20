package me.coonect.coonect.mock;

import jakarta.mail.MessagingException;
import me.coonect.coonect.member.application.port.out.infrastructure.EmailVerificationMailSender;

public class FakeEmailVerificationMailSender implements EmailVerificationMailSender {

  public String email;
  public String code;

  public String exceptionEmail;

  public FakeEmailVerificationMailSender(String exceptionEmail) {
    this.exceptionEmail = exceptionEmail;
  }

  @Override
  public void send(String email, String code) throws MessagingException {
    if (email.equals(exceptionEmail)) {
      throw new MessagingException();
    }

    this.email = email;
    this.code = code;
  }
}
