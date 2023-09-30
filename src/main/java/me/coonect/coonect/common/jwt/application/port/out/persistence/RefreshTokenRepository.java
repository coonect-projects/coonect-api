package me.coonect.coonect.common.jwt.application.port.out.persistence;

import java.util.Optional;

public interface RefreshTokenRepository {

  /**
   * refresh token을 저장한다. username에 refresh token이 이미 존재하면 값을 업데이트 한다.
   */
  void update(String username, String refreshToken);

  Optional<String> find(String username);
}
