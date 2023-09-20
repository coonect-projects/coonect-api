package me.coonect.coonect.member.adapter.in.web.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.port.in.SendVerificationEmailCommand;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SendVerificationMailRequest {

  private String email;

  public SendVerificationEmailCommand toCommand() {
    return new SendVerificationEmailCommand(email);
  }
}
