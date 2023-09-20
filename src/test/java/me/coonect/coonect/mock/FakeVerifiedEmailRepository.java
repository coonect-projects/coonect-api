package me.coonect.coonect.mock;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import me.coonect.coonect.member.application.port.out.persistence.VerifiedEmailRepository;

public class FakeVerifiedEmailRepository implements VerifiedEmailRepository {

  private Map<String, String> data = new HashMap<>();

  @Override
  public void save(String email, String code, Duration expireDuration) {
    data.put(email, code);
  }

  @Override
  public String get(String email) {
    return data.get(email);
  }

  @Override
  public void remove(String email) {
    data.remove(email);
  }

  @Override
  public boolean has(String email) {
    return data.containsKey(email);
  }
}
