package me.coonect.coonect.common.security.authentication.entrypoint;

import static org.assertj.core.api.Assertions.assertThat;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.coonect.coonect.common.jwt.exception.UnexpectedRefreshTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtAuthenticationEntryPointTest {

  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @BeforeEach
  public void init() {
    jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint(new ObjectMapper());
  }

  @Test
  public void 토큰이_만료되었다면_499_응답을_보낸다() throws Exception {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    request.setAttribute("exception", new TokenExpiredException("", null));

    // when
    jwtAuthenticationEntryPoint.commence(request, response,
        new AuthenticationCredentialsNotFoundException(""));

    // then
    assertThat(response.getStatus()).isEqualTo(499);
  }

  @Test
  public void 요청_refresh_token_과_저장된_refresh_token_이_다르면_401_응답을_보낸다() throws Exception {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    request.setAttribute("exception", new UnexpectedRefreshTokenException());

    // when
    jwtAuthenticationEntryPoint.commence(request, response,
        new AuthenticationCredentialsNotFoundException(""));

    // then
    assertThat(response.getStatus()).isEqualTo(401);
  }

  @Test
  public void 토큰_문제로_commence_된_것이라면_401_응답을_보낸다() throws Exception {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    request.setAttribute("exception", new JWTVerificationException("", null));

    // when
    jwtAuthenticationEntryPoint.commence(request, response,
        new AuthenticationCredentialsNotFoundException(""));

    // then
    assertThat(response.getStatus()).isEqualTo(401);
  }

  @Test
  public void 토큰_외의_문제로_commence_된_것이라면_401_응답을_보낸다() throws Exception {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();

    // when
    jwtAuthenticationEntryPoint.commence(request, response,
        new AuthenticationCredentialsNotFoundException(""));

    // then
    assertThat(response.getStatus()).isEqualTo(401);
  }

}