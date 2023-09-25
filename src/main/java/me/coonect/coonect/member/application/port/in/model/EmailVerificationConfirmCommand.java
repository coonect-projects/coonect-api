package me.coonect.coonect.member.application.port.in.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import me.coonect.coonect.common.validation.Validator;
import me.coonect.coonect.member.application.port.in.model.validation.EmailVerificationCode;

@Getter
public class EmailVerificationConfirmCommand {

  @Email
  @NotBlank
  String email;

  @EmailVerificationCode
  String code;

  public EmailVerificationConfirmCommand(String email, String code) {
    this.email = email;
    this.code = code;

    Validator.validate(this);
  }
}
