package me.coonect.coonect.common.jwt.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.common.jwt.application.port.out.persistence.RefreshTokenRepository;
import me.coonect.coonect.common.jwt.exception.UnexpectedRefreshTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class JwtService {

  private static final String ACCESS_TOKEN_SUBJECT = "access-token";
  private static final String REFRESH_TOKEN_SUBJECT = "refresh-token";
  private static final String USERNAME_CLAIM = "email";
  private static final String ROLES_CLAIM = "roles";

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtProperties jwtProperties;

  @Transactional
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

  @Transactional
  public TokenResponse reissue(String refreshToken) {
    Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
    JWTVerifier verifier = JWT.require(algorithm).build();

    DecodedJWT decodedToken = verifier.verify(refreshToken);

    String username = decodedToken.getClaim(USERNAME_CLAIM).asString();
    List<String> roles = decodedToken.getClaim(ROLES_CLAIM).asList(String.class);

    Optional<String> savedRefreshToken = refreshTokenRepository.find(username);

    if (savedRefreshToken.isEmpty() || !refreshToken.equals(savedRefreshToken.get())) {
      refreshTokenRepository.delete(username);
      throw new UnexpectedRefreshTokenException();
    }

    String newRefreshToken = generateRefreshToken(username, roles);
    String newAccessToken = generateAccessToken(username, roles);

    return new TokenResponse(jwtProperties.getTokenType(),
        newAccessToken,
        jwtProperties.getAccessTokenExpirationSeconds(),
        newRefreshToken,
        jwtProperties.getRefreshTokenExpirationSeconds());
  }

  public Authentication resolveAccessToken(String accessToken) {
    Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
    JWTVerifier verifier = JWT.require(algorithm).withSubject(ACCESS_TOKEN_SUBJECT).build();
    DecodedJWT decodeToken = verifier.verify(accessToken);

    String username = decodeToken.getClaim(USERNAME_CLAIM).asString();
    List<SimpleGrantedAuthority> roles = decodeToken.getClaim(ROLES_CLAIM)
        .asList(String.class)
        .stream()
        .map(SimpleGrantedAuthority::new)
        .toList();

    return UsernamePasswordAuthenticationToken.authenticated(username, null, roles);
  }
}
