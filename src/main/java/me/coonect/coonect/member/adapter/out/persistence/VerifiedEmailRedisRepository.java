package me.coonect.coonect.member.adapter.out.persistence;

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
  public void save(String code, String email) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    ops.set(KEY_PREFIX + code, email);
  }

  @Override
  public String get(String code) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    return ops.get(KEY_PREFIX + code);
  }

  @Override
  public void remove(String code) {
    redisTemplate.delete(KEY_PREFIX + code);
  }

  @Override
  public boolean has(String code) {
    return redisTemplate.hasKey(KEY_PREFIX + code);
  }
}
