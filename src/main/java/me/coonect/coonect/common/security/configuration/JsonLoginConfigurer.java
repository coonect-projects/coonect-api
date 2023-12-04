package me.coonect.coonect.common.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.coonect.coonect.common.security.login.filter.JsonLoginProcessingFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

/**
 * {@link JsonLoginProcessingFilter}를 설정한다. {@link AuthenticationSuccessHandler}와 {@link AuthenticationFailureHandler}의 설정이 필수이다.
 */
public class JsonLoginConfigurer extends AbstractHttpConfigurer<JsonLoginConfigurer, HttpSecurity> {

  private final JsonLoginProcessingFilter authFilter;

  private AuthenticationSuccessHandler successHandler;
  private AuthenticationFailureHandler failureHandler;

  public JsonLoginConfigurer(ObjectMapper objectMapper) {
    this.authFilter = new JsonLoginProcessingFilter(objectMapper);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    Assert.notNull(successHandler, "successHandler cannot be null");
    Assert.notNull(failureHandler, "failureHandler cannot be null");

    this.authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
    this.authFilter.setAuthenticationSuccessHandler(this.successHandler);
    this.authFilter.setAuthenticationFailureHandler(this.failureHandler);
    SessionAuthenticationStrategy sessionAuthenticationStrategy = http
        .getSharedObject(SessionAuthenticationStrategy.class);
    if (sessionAuthenticationStrategy != null) {
      this.authFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
    }
    // TODO : SecurityContextRepository 설정 (AbstractAuthenticationProcessingFilter 에 기본값 설정 있음)
    this.authFilter.setSecurityContextHolderStrategy(getSecurityContextHolderStrategy());
    JsonLoginProcessingFilter filter = postProcess(this.authFilter);
    http.addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
  }

  public JsonLoginConfigurer loginProcessingUrl(String loginProcessingUrl) {
    this.authFilter.setRequiresAuthenticationRequestMatcher(
        createLoginProcessingUrlMatcher(loginProcessingUrl));
    return this;
  }

  public final JsonLoginConfigurer successHandler(AuthenticationSuccessHandler successHandler) {
    this.successHandler = successHandler;
    return this;
  }

  public final JsonLoginConfigurer failureHandler(
      AuthenticationFailureHandler authenticationFailureHandler) {
    this.failureHandler = authenticationFailureHandler;
    return this;
  }

  private RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
    return new AntPathRequestMatcher(loginProcessingUrl, "POST");
  }
}
