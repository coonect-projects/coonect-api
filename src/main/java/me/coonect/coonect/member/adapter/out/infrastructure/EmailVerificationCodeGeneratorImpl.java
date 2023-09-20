package me.coonect.coonect.member.adapter.out.infrastructure;

import me.coonect.coonect.member.application.port.out.infrastructure.EmailVerificationCodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationCodeGeneratorImpl implements EmailVerificationCodeGenerator {

  @Override
  public String generate() {
    int number = (int) (Math.random() * 1000000);
    return String.format("%06d", number);
  }
}
