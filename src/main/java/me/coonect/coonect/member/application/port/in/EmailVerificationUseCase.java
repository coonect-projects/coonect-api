package me.coonect.coonect.member.application.port.in;

import me.coonect.coonect.member.application.port.in.model.EmailVerificationConfirmCommand;
import me.coonect.coonect.member.application.port.in.model.SendVerificationEmailCommand;

public interface EmailVerificationUseCase {

  void sendVerificationEmail(SendVerificationEmailCommand command);

  boolean verifyEmail(EmailVerificationConfirmCommand command);
}
