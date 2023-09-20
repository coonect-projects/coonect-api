package me.coonect.coonect.member.application.port.in.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import me.coonect.coonect.common.validation.Validator;

@Getter
public class SendVerificationEmailCommand {

  @Email
  @NotBlank
  String email;

  public SendVerificationEmailCommand(String email) {
    this.email = email;

    Validator.validate(this);
  }
}
