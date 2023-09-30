package me.coonect.coonect.common.security.login.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.coonect.coonect.common.jwt.application.service.JwtProperties;
import me.coonect.coonect.common.jwt.application.service.JwtService;
import me.coonect.coonect.mock.FakeRefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtAuthenticationSuccessHandlerTest {

  private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

  private JwtProperties jwtProperties;
  private JwtService jwtService;
  private ObjectMapper objectMapper;

  @BeforeEach
  public void init() {

    jwtProperties = JwtProperties.builder()
        .secret("qweasd")
        .accessTokenExpirationSeconds(100L)
        .refreshTokenExpirationSeconds(1000L)
        .tokenType("Bearer")
        .build();

    jwtService = new JwtService(new FakeRefreshTokenRepository(), jwtProperties);

    objectMapper = new ObjectMapper();

    jwtAuthenticationSuccessHandler = new JwtAuthenticationSuccessHandler(
        jwtService, objectMapper);

  }

  @Test
  public void 로그인_성공_시_200_OK_응답을_보낸다() throws Exception {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    Authentication authentication = new TestingAuthenticationToken(User.builder()
        .username("duk9741@gmail.com")
        .password("123123")
        .roles("USER")
        .build(), null);

    // when
    jwtAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

    // then
    response.getOutputStream();
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);

  }


}