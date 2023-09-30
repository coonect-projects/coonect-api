package me.coonect.coonect.common.jwt.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import me.coonect.coonect.common.jwt.application.port.out.persistence.RefreshTokenRepository;
import me.coonect.coonect.mock.FakeRefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtServiceTest {

  private JwtService jwtService;
  private RefreshTokenRepository refreshTokenRepository;

  @BeforeEach
  public void init() {
    refreshTokenRepository = new FakeRefreshTokenRepository();
    jwtService = new JwtService(refreshTokenRepository, new JwtProperties(
        "secret-secret",
        100L,
        200L,
        "Bearer"
    ));
  }

  @Test
  public void 토큰을_발급할_수_있다() throws Exception {
    // given
    // when
    TokenResponse tokenResponse = jwtService.generateTokens("duk9741@gmail.com", List.of("MEMBER"));

    // then
    assertThat(tokenResponse.getTokenType()).isEqualTo("Bearer");
    assertThat(tokenResponse.getAccessToken()).isNotBlank();
    assertThat(tokenResponse.getAccessTokenExpiresIn()).isEqualTo(100L);
    assertThat(tokenResponse.getRefreshToken()).isNotBlank();
    assertThat(tokenResponse.getRefreshTokenExpiresIn()).isEqualTo(200L);

  }

  @Test
  public void 리프레시_토큰을_발급하면_저장된다() throws Exception {
    // given
    // when
    TokenResponse tokenResponse = jwtService.generateTokens("duk9741@gmail.com", List.of("MEMBER"));

    // then
    assertThat(refreshTokenRepository.find("duk9741@gmail.com")).isNotEmpty();

  }

}