package me.coonect.coonect.common.jwt.application.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class JwtProperties {

  @Value("${coonect.jwt.properties.secret}")
  private String secret;
  @Value("${coonect.jwt.properties.access-token-expiration-seconds}")
  private Long accessTokenExpirationSeconds;
  @Value("${coonect.jwt.properties.refresh-token-expiration-seconds}")
  private Long refreshTokenExpirationSeconds;
  @Value("${coonect.jwt.properties.token-type}")
  private String tokenType;

}
