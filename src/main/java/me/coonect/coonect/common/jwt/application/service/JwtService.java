package me.coonect.coonect.common.jwt.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.common.jwt.application.port.out.persistence.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService {

  private static final String ACCESS_TOKEN_SUBJECT = "access-token";
  private static final String REFRESH_TOKEN_SUBJECT = "refresh-token";
  private static final String USERNAME_CLAIM = "email";
  private static final String ROLES_CLAIM = "roles";

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtProperties jwtProperties;

  public TokenResponse generateTokens(String username, List<String> roles) {
    String assessToken = generateAccessToken(username, roles);
    String refreshToken = generateRefreshToken(username, roles);

    return new TokenResponse(jwtProperties.getTokenType(),
        assessToken,
        jwtProperties.getAccessTokenExpirationSeconds(),
        refreshToken,
        jwtProperties.getRefreshTokenExpirationSeconds());
  }

  private String generateAccessToken(String username, List<String> roles) {
    return generateTokenWithSubject(ACCESS_TOKEN_SUBJECT, username, roles);
  }

  private String generateRefreshToken(String username, List<String> roles) {
    String refreshToken = generateTokenWithSubject(REFRESH_TOKEN_SUBJECT, username, roles);
    refreshTokenRepository.update(username, refreshToken);
    return refreshToken;
  }

  private String generateTokenWithSubject(String subject, String username, List<String> roles) {
    Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
    return JWT.create().withSubject(subject)
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .withExpiresAt(new Date(System.currentTimeMillis()
            + jwtProperties.getAccessTokenExpirationSeconds() * 1000L))
        .withClaim(USERNAME_CLAIM, username)
        .withClaim(ROLES_CLAIM, roles)
        .sign(algorithm);
  }

}
