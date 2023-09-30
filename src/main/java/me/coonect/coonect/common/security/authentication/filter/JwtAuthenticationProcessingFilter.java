package me.coonect.coonect.common.security.authentication.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.coonect.coonect.common.jwt.application.service.JwtProperties;
import me.coonect.coonect.common.jwt.application.service.JwtService;
import me.coonect.coonect.common.jwt.application.service.TokenResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

  private final static String accessTokenHeaderName = "Authorization";
  private final static String refreshTokenHeaderName = "Refresh-Token";

  private final JwtService jwtService;
  private final JwtProperties jwtProperties;
  private final ObjectMapper objectMapper;

  @Override
  /**
   * 요청 Refresh-Token 헤더에 값이 존재하면 재발급 요청이라고 가정하고 재발급 프로세스를 진행한다.
   * Refresh-Token 헤더가 존재하지 않으면 인증 요청이므로 인증 프로세스를 진핸한다.
   */
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String refreshToken = extractRefreshToken(request);
    String accessToken = extractAccessToken(request);

    // refresh token 포함 재발급의 경우
    if (StringUtils.hasText(refreshToken)) {
      try {
        TokenResponse tokenResponse = jwtService.reissue(refreshToken);
        tokenResponse.sendReissueResponse(response, objectMapper);
        return;
      } catch (JWTVerificationException e) {
        request.setAttribute("exception", e);
      }

      filterChain.doFilter(request, response);
      return;
    }

    // access-token 없으면 pass
    if (!StringUtils.hasText(accessToken)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      Authentication authentication = jwtService.resolveAccessToken(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (JWTVerificationException e) {
      // 인증이 실패했을 경우 request에 예외 저장
      request.setAttribute("exception", e);
    }

    filterChain.doFilter(request, response);
  }

  private String extractRefreshToken(HttpServletRequest request) {
    return extractToken(request, refreshTokenHeaderName);
  }

  private String extractAccessToken(HttpServletRequest request) {
    return extractToken(request, accessTokenHeaderName);
  }

  private String extractToken(HttpServletRequest request, String headerName) {
    String refreshTokenWithType = request.getHeader(headerName);

    if (!StringUtils.hasText(refreshTokenWithType) ||
        !refreshTokenWithType.startsWith(jwtProperties.getTokenType() + " ")) {
      return null;
    }
    return refreshTokenWithType.substring(jwtProperties.getTokenType().length() + 1);
  }

}
