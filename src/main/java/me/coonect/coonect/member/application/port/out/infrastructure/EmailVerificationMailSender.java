package me.coonect.coonect.member.application.port.out.infrastructure;

import jakarta.mail.MessagingException;

public interface EmailVerificationMailSender {

  void send(String email, String code) throws MessagingException;

}
