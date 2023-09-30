package me.coonect.coonect.common.security.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.common.jwt.application.service.JwtService;
import me.coonect.coonect.common.jwt.application.service.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::toString)
        .toList();

    TokenResponse tokenResponse = jwtService.generateTokens(username, roles);
    String body = objectMapper.writeValueAsString(tokenResponse);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(body);
    response.setStatus(HttpStatus.OK.value());
  }
}
