package me.coonect.coonect.member.adapter.in.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.coonect.coonect.member.application.port.in.EmailVerificationConfirmCommand;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerificationConfirmRequest {

  private String email;
  private String code;


  public EmailVerificationConfirmCommand toCommand() {
    return new EmailVerificationConfirmCommand(email, code);
  }
}
