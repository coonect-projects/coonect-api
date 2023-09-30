package me.coonect.coonect.common.jwt.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TokenResponse {

  private String tokenType;
  private String accessToken;
  private Long accessTokenExpiresIn;
  private String refreshToken;
  private Long refreshTokenExpiresIn;

  public void sendLoginResponse(HttpServletResponse response, ObjectMapper objectMapper)
      throws IOException {
    send(response, objectMapper, HttpServletResponse.SC_OK);
  }

  private void send(HttpServletResponse response, ObjectMapper objectMapper, int status)
      throws IOException {
    String body = objectMapper.writeValueAsString(this);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(body);
    response.setStatus(status);
  }
}
