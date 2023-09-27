package me.coonect.coonect.common.security.login.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JsonLoginProcessingFilterTest {

  private JsonLoginProcessingFilter jsonLoginProcessingFilter;
  private AuthenticationManager authenticationManager;
  private ObjectMapper objectMapper;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  public void init() {
    objectMapper = new ObjectMapper();
    jsonLoginProcessingFilter = new JsonLoginProcessingFilter(objectMapper);

    authenticationManager = mock(AuthenticationManager.class);

    jsonLoginProcessingFilter.setAuthenticationManager(authenticationManager);

    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @Test
  public void POST_메서드가_아니면_예외를_발생시킨다() throws Exception {
    // given
    request.setMethod("GET");
    request.setContentType(MediaType.APPLICATION_JSON_VALUE);

    // when
    // then
    Assertions.assertThatThrownBy(() -> {
      jsonLoginProcessingFilter.attemptAuthentication(request, response);
    }).isInstanceOf(AuthenticationServiceException.class);
  }

  @Test
  public void Content_Type_이_json_이_아니면_예외를_발생시킨다() throws Exception {
    // given
    request.setMethod("POST");
    request.setContentType(MediaType.APPLICATION_PDF_VALUE);

    // when
    // then
    Assertions.assertThatThrownBy(() -> {
      jsonLoginProcessingFilter.attemptAuthentication(request, response);
    }).isInstanceOf(AuthenticationServiceException.class);
  }

  @Test
  public void email_과_password_가_둘다_존재하면_인증을_시도한다() throws Exception {
    // given
    Map<String, String> body = new HashMap<>();
    body.put("email", "duk9741@gmail.com");
    body.put("password", "123qwe456rty");

    request.setContentType(MediaType.APPLICATION_JSON_VALUE);
    request.setMethod("POST");
    request.setContent(objectMapper.writeValueAsBytes(body));

    // when
    jsonLoginProcessingFilter.attemptAuthentication(request, response);

    // then
    verify(authenticationManager).authenticate(any(Authentication.class));
  }

  @Test
  public void email_이_없어도_인증을_시도한다() throws Exception {
    // given
    Map<String, String> body = new HashMap<>();
    body.put("password", "123qwe456rty");

    request.setContentType(MediaType.APPLICATION_JSON_VALUE);
    request.setMethod("POST");
    request.setContent(objectMapper.writeValueAsBytes(body));

    // when
    jsonLoginProcessingFilter.attemptAuthentication(request, response);

    // then
    verify(authenticationManager).authenticate(any(Authentication.class));
  }

  @Test
  public void password_가_없어도_인증을_시도한다() throws Exception {
    // given
    Map<String, String> body = new HashMap<>();
    body.put("email", "duk9741@gmail.com");

    request.setContentType(MediaType.APPLICATION_JSON_VALUE);
    request.setMethod("POST");
    request.setContent(objectMapper.writeValueAsBytes(body));

    // when
    jsonLoginProcessingFilter.attemptAuthentication(request, response);

    // then
    verify(authenticationManager).authenticate(any(Authentication.class));
  }

}