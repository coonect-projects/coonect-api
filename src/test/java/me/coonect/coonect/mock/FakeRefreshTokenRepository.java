package me.coonect.coonect.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import me.coonect.coonect.common.jwt.application.port.out.persistence.RefreshTokenRepository;

public class FakeRefreshTokenRepository implements RefreshTokenRepository {

  private Map<String, String> data = new HashMap<>();

  @Override
  public void update(String username, String refreshToken) {
    data.put(username, refreshToken);
  }

  @Override
  public Optional<String> find(String username) {
    return Optional.ofNullable(data.get(username));
  }

  @Override
  public void delete(String username) {
    data.remove(username);
  }
}
