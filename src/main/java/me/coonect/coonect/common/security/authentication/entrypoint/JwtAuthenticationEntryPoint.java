package me.coonect.coonect.common.security.authentication.entrypoint;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.coonect.coonect.common.error.ErrorResponse;
import me.coonect.coonect.common.error.exception.ErrorCode;
import me.coonect.coonect.common.jwt.exception.UnexpectedRefreshTokenException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;


  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    JWTVerificationException jwtVerificationException =
        (JWTVerificationException) request.getAttribute("exception");

    if (jwtVerificationException instanceof TokenExpiredException) {
      sendErrorResponse(response, ErrorCode.TOKEN_EXPIRED, 499);
      return;
    }

    if (jwtVerificationException instanceof UnexpectedRefreshTokenException) {
      sendErrorResponse(response, ErrorCode.UNEXPECTED_REFRESH_TOKEN,
          HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    if (jwtVerificationException != null) {
      sendErrorResponse(response, ErrorCode.INVALID_TOKEN, HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    sendErrorResponse(response, ErrorCode.UNAUTHORIZED, HttpServletResponse.SC_UNAUTHORIZED);
  }

  private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode, int status)
      throws IOException {
    ErrorResponse errorResponse = ErrorResponse.of(errorCode);

    String body = objectMapper.writeValueAsString(errorResponse);
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(body);
  }

}
