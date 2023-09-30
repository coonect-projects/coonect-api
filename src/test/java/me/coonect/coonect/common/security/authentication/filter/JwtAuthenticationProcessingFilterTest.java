package me.coonect.coonect.common.security.authentication.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import me.coonect.coonect.common.jwt.application.port.out.persistence.RefreshTokenRepository;
import me.coonect.coonect.common.jwt.application.service.JwtProperties;
import me.coonect.coonect.common.jwt.application.service.JwtService;
import me.coonect.coonect.common.jwt.application.service.TokenResponse;
import me.coonect.coonect.common.jwt.exception.UnexpectedRefreshTokenException;
import me.coonect.coonect.mock.FakeRefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtAuthenticationProcessingFilterTest {

  private JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;

  private JwtProperties jwtProperties;

  private JwtService jwtService;
  private RefreshTokenRepository refreshTokenRepository;

  private ObjectMapper objectMapper;

  @BeforeEach
  public void init() {
    SecurityContextHolder.clearContext();

    jwtProperties = new JwtProperties(
        "qweasd",
        100L,
        200L,
        "Bearer"
    );

    refreshTokenRepository = new FakeRefreshTokenRepository();

    jwtService = new JwtService(refreshTokenRepository, jwtProperties);

    objectMapper = new ObjectMapper();

    jwtAuthenticationProcessingFilter
        = new JwtAuthenticationProcessingFilter(jwtService, jwtProperties, objectMapper);

  }

  @Test
  public void Refresh_Token_이_헤더에_존재하면_RESET_CONTENT_응답과_Token_들을_모두_재발급_한다() throws Exception {
    // given
    // refresh token 저장
    jwtService.generateTokens("duk9741@gmail.com", List.of("MEMBER"));
    String refreshToken = refreshTokenRepository.find("duk9741@gmail.com").get();

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Refresh-Token", "Bearer " + refreshToken);
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    // when
    jwtAuthenticationProcessingFilter.doFilterInternal(request, response, chain);

    // then
    verify(chain, times(0)).doFilter(any(ServletRequest.class), any(ServletResponse.class));

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_RESET_CONTENT);

    TokenResponse tokenResponse = objectMapper.readValue(response.getContentAsString(),
        TokenResponse.class);

    assertThat(tokenResponse).isNotNull();
    assertThat(tokenResponse.getAccessToken()).isNotNull();
    assertThat(tokenResponse.getRefreshToken()).isNotNull();
    assertThat(tokenResponse.getAccessTokenExpiresIn()).isEqualTo(100L);
    assertThat(tokenResponse.getRefreshTokenExpiresIn()).isEqualTo(200L);

  }

  @Test
  public void Refresh_Token_이_저장된_것과_다르면_request_에_exception_을_저장한다() throws Exception {
    // given
    // refresh token 저장
    jwtService.generateTokens("duk9741@gmail.com", List.of("MEMBER"));
    String refreshToken = JWT.create().withSubject("refresh-token")
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .withExpiresAt(new Date(System.currentTimeMillis()
            + jwtProperties.getRefreshTokenExpirationSeconds() * 1000L + 1000))
        .withClaim("email", "duk9741@gmail.com")
        .withClaim("roles", "MEMBER")
        .sign(Algorithm.HMAC256(jwtProperties.getSecret()));

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Refresh-Token", "Bearer " + refreshToken);
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    // when
    jwtAuthenticationProcessingFilter.doFilterInternal(request, response, chain);

    // then
    verify(chain, times(1)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(request.getAttribute("exception")).isInstanceOf(
        UnexpectedRefreshTokenException.class);
    assertThat(response.getContentLength()).isEqualTo(0);

  }

  @Test
  public void Refresh_Token_이_만료_되었다면_request_에_exception_을_저장한다() throws Exception {
    // given
    String refreshToken = JWT.create().withSubject("refresh-token")
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .withExpiresAt(new Date(System.currentTimeMillis() - 1000L))
        .withClaim("email", "duk9741@gmail.com")
        .withClaim("roles", "MEMBER")
        .sign(Algorithm.HMAC256(jwtProperties.getSecret()));

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Refresh-Token", "Bearer " + refreshToken);
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    // when
    jwtAuthenticationProcessingFilter.doFilterInternal(request, response, chain);

    // then
    verify(chain, times(1)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(request.getAttribute("exception")).isInstanceOf(
        TokenExpiredException.class);
    assertThat(response.getContentLength()).isEqualTo(0);

  }

  @Test
  public void 잘못된_Secret_의_Refresh_Token_이라면_request_에_exception_을_저장한다() throws Exception {
    // given
    String refreshToken = JWT.create().withSubject("refresh-token")
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .withExpiresAt(new Date(System.currentTimeMillis() - 1000L))
        .withClaim("email", "duk9741@gmail.com")
        .withClaim("roles", "MEMBER")
        .sign(Algorithm.HMAC256("xxxxxxxxxxxx"));

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Refresh-Token",
        "Bearer " + refreshToken);
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    // when
    jwtAuthenticationProcessingFilter.doFilterInternal(request, response, chain);

    // then
    verify(chain, times(1)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(request.getAttribute("exception")).isInstanceOf(
        JWTVerificationException.class);
    assertThat(response.getContentLength()).isEqualTo(0);

  }


  @Test
  public void Access_Token_이_헤더에_존재하면_SecurityContext_에_인증_객체를_저장한다() throws Exception {
    // given
    TokenResponse tokenResponse = jwtService.generateTokens("duk9741@gmail.com",
        List.of("MEMBER"));
    String accessToken = tokenResponse.getAccessToken();

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer " + accessToken);
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    // when
    jwtAuthenticationProcessingFilter.doFilterInternal(request, response, chain);

    // then
    verify(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    assertThat(response.getContentLength()).isEqualTo(0);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    assertThat(SecurityContextHolder.getContext().getAuthentication().getName())
        .isEqualTo("duk9741@gmail.com");

  }

  @Test
  public void Access_Token_이_만료되었다면_request_에_exception_을_저장한다() throws Exception {
    // given
    String accessToken = JWT.create().withSubject("access-token")
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .withExpiresAt(new Date(System.currentTimeMillis() - 1000L))
        .withClaim("email", "duk9741@gmail.com")
        .withClaim("roles", "MEMBER")
        .sign(Algorithm.HMAC256(jwtProperties.getSecret()));

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer " + accessToken);
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    // when
    jwtAuthenticationProcessingFilter.doFilterInternal(request, response, chain);

    // then
    verify(chain, times(1)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(request.getAttribute("exception")).isInstanceOf(
        TokenExpiredException.class);
    assertThat(
        response.containsHeader("Refresh-Token")).isFalse();
    assertThat(response.containsHeader("Authorization")).isFalse();

  }

  @Test
  public void 잘못된_Secret_의_Access_Token_이라면_request_에_exception_을_저장한다() throws Exception {
    // given
    String accessToken = JWT.create().withSubject("access-token")
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .withExpiresAt(new Date(System.currentTimeMillis() - 1000L))
        .withClaim("email", "duk9741@gmail.com")
        .withClaim("roles", "MEMBER")
        .sign(Algorithm.HMAC256("xxxxxxxxxxxx"));

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization",
        "Bearer " + accessToken);
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    // when
    jwtAuthenticationProcessingFilter.doFilterInternal(request, response, chain);

    // then
    verify(chain, times(1)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(request.getAttribute("exception")).isInstanceOf(
        JWTVerificationException.class);
    assertThat(response.getContentLength()).isEqualTo(0);

  }

  @Test
  public void 토큰이_모두_존재하지_않는다면_필터를_지나간다() throws Exception {
    // given

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    // when
    jwtAuthenticationProcessingFilter.doFilterInternal(request, response, chain);

    // then
    verify(chain, times(1)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(request.getAttribute("exception")).isNull();
    assertThat(response.getContentLength()).isEqualTo(0);
  }


}