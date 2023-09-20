package me.coonect.coonect.mock;

import java.util.HashMap;
import java.util.Map;
import me.coonect.coonect.member.application.port.out.persistence.VerifiedEmailRepository;

public class FakeVerifiedEmailRepository implements VerifiedEmailRepository {

  private Map<String, String> data = new HashMap<>();

  @Override
  public void save(String code, String email) {
    data.put(code, email);
  }

  @Override
  public String get(String code) {
    return data.get(code);
  }

  @Override
  public void remove(String code) {
    data.remove(code);
  }

  @Override
  public boolean has(String code) {
    return data.containsKey(code);
  }
}
