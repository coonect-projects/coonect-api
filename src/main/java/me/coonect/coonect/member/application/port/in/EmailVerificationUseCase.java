package me.coonect.coonect.member.application.port.in;

public interface EmailVerificationUseCase {

  void sendVerificationEmail(SendVerificationEmailCommand command);

  boolean verifyEmail(EmailVerificationConfirmCommand command);
}
