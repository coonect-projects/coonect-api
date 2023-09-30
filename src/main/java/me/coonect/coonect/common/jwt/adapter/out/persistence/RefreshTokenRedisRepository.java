package me.coonect.coonect.common.jwt.adapter.out.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.common.jwt.application.port.out.persistence.RefreshTokenRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRedisRepository implements RefreshTokenRepository {

  private static final String KEY_PREFIX = "refreshToken:";
  private final StringRedisTemplate redisTemplate;

  @Override
  public void update(String username, String refreshToken) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    ops.set(KEY_PREFIX + username, refreshToken);
  }

  @Override
  public Optional<String> find(String username) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    return Optional.ofNullable(ops.get(KEY_PREFIX + username));
  }

  @Override
  public void delete(String username) {
    redisTemplate.delete(KEY_PREFIX + username);
  }

}
