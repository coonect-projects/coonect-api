package me.coonect.coonect.member.adapter.in.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.port.in.EmailVerificationConfirmCommand;
import me.coonect.coonect.member.application.port.in.EmailVerificationUseCase;
import me.coonect.coonect.member.application.port.in.SendVerificationEmailCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailVerificationController {

  private final EmailVerificationUseCase emailVerificationUseCase;

  @PostMapping("/member/email/verification/request")
  public ResponseEntity<Void> sendVerificationMail(
      @RequestBody SendVerificationMailRequest request) {
    SendVerificationEmailCommand command = request.toCommand();
    emailVerificationUseCase.sendVerificationEmail(command);
    return ResponseEntity.status(HttpServletResponse.SC_CREATED).build();
  }

  @PostMapping("/member/email/verification/confirm")
  public ResponseEntity<Void> confirmEmailVerification(
      @RequestBody EmailVerificationConfirmRequest request) {

    EmailVerificationConfirmCommand command = request.toCommand();

    if (emailVerificationUseCase.verifyEmail(command)) {
      return ResponseEntity.ok().build();
    }

    return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).build();
  }
}
