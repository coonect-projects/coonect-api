package me.coonect.coonect.member.adapter.out.persistence;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.port.out.persistence.VerifiedEmailRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class VerifiedEmailRedisRepository implements VerifiedEmailRepository {


  private static final String KEY_PREFIX = "validatedEmail:";
  private final StringRedisTemplate redisTemplate;

  @Override
  public void save(String email, String code, Duration expireDuration) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    ops.set(KEY_PREFIX + email, code);
  }

  @Override
  public String get(String email) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    return ops.get(KEY_PREFIX + email);
  }

  @Override
  public void remove(String email) {
    redisTemplate.delete(KEY_PREFIX + email);
  }

  @Override
  public boolean has(String email) {
    return redisTemplate.hasKey(KEY_PREFIX + email);
  }
}
