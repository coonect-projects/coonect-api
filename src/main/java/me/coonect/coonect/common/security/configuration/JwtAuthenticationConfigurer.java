package me.coonect.coonect.common.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import me.coonect.coonect.common.jwt.application.service.JwtProperties;
import me.coonect.coonect.common.jwt.application.service.JwtService;
import me.coonect.coonect.common.security.authentication.entrypoint.JwtAuthenticationEntryPoint;
import me.coonect.coonect.common.security.authentication.filter.JwtAuthenticationProcessingFilter;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

/**
 * {@link JwtAuthenticationProcessingFilter} 를 설정한다. {@link AuthenticationEntryPoint}를 설정하지 않으면 기본적으로 {@link JwtAuthenticationEntryPoint}를 설정한다.
 */
public class JwtAuthenticationConfigurer extends
    AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

  private final JwtAuthenticationProcessingFilter authFilter;
  private AuthenticationEntryPoint authenticationEntryPoint;

  public JwtAuthenticationConfigurer(JwtService jwtService, JwtProperties jwtProperties,
      ObjectMapper objectMapper) {
    this.authFilter = new JwtAuthenticationProcessingFilter(jwtService, jwtProperties,
        objectMapper);
    authenticationEntryPoint = new JwtAuthenticationEntryPoint(objectMapper);
  }

  @SuppressWarnings("unchecked")
  protected final void registerAuthenticationEntryPoint(HttpSecurity http,
      AuthenticationEntryPoint authenticationEntryPoint) {
    var exceptionHandling = http.getConfigurer(ExceptionHandlingConfigurer.class);
    if (exceptionHandling == null) {
      return;
    }
    exceptionHandling.defaultAuthenticationEntryPointFor(postProcess(authenticationEntryPoint),
        getAuthenticationEntryPointMatcher(http));
  }

  @Override
  public void init(HttpSecurity http) throws Exception {
    registerDefaultAuthenticationEntryPoint(http);
  }

  protected final void registerDefaultAuthenticationEntryPoint(HttpSecurity http) {
    registerAuthenticationEntryPoint(http, this.authenticationEntryPoint);
  }

  protected final RequestMatcher getAuthenticationEntryPointMatcher(HttpSecurity http) {
    ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(
        ContentNegotiationStrategy.class);
    if (contentNegotiationStrategy == null) {
      contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
    }
    MediaTypeRequestMatcher mediaMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy,
        MediaType.APPLICATION_XHTML_XML, new MediaType("image", "*"), MediaType.TEXT_HTML,
        MediaType.TEXT_PLAIN);
    mediaMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
    RequestMatcher notXRequestedWith = new NegatedRequestMatcher(
        new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
    return new AndRequestMatcher(Arrays.asList(notXRequestedWith, mediaMatcher));
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.addFilterAfter(authFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
