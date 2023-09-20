package me.coonect.coonect.member.application.port.out.persistence;

import java.time.Duration;

public interface EmailVerificationCodeRepository {

  void save(String email, String code, Duration expireDuration);

  String get(String email);

  void remove(String email);

  boolean has(String email);
}
