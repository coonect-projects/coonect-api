package me.coonect.coonect.common.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.common.jwt.application.service.JwtProperties;
import me.coonect.coonect.common.jwt.application.service.JwtService;
import me.coonect.coonect.common.security.login.handler.ErrorResponseAuthenticationFailureHandler;
import me.coonect.coonect.common.security.login.handler.JwtAuthenticationSuccessHandler;
import me.coonect.coonect.common.security.login.service.UserDetailsServiceImpl;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfiguration {

  private static final RequestMatcher[] ALLOWED_REQUESTS = {
      new AntPathRequestMatcher("/error", "GET"),
      new AntPathRequestMatcher("/member", "POST"),
      new AntPathRequestMatcher("/member/nickname/valid", "GET"),
      new AntPathRequestMatcher("/member/email/verification/request", "POST"),
      new AntPathRequestMatcher("/member/email/verification/confirm", "POST"),
      new AntPathRequestMatcher("/login", "POST")
  };

  private final ObjectMapper objectMapper;
  private final MemberRepository memberRepository;
  private final JwtService jwtService;
  private final JwtProperties jwtProperties;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(request -> request
            .requestMatchers(ALLOWED_REQUESTS).permitAll()
            .anyRequest().authenticated())
        .logout(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .requestCache(RequestCacheConfigurer::disable)
        .sessionManagement(AbstractHttpConfigurer::disable);

    http.userDetailsService(userDetailsService());

    http.apply(jsonLogin()
        .successHandler(authenticationSuccessHandler())
        .failureHandler(authenticationFailureHandler()));

    http.apply(jwtAuthentication());

    return http.build();
  }

  private JsonLoginConfigurer jsonLogin() {
    return new JsonLoginConfigurer(objectMapper);
  }

  private JwtAuthenticationConfigurer jwtAuthentication() {
    return new JwtAuthenticationConfigurer(jwtService, jwtProperties, objectMapper);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl(memberRepository);
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new JwtAuthenticationSuccessHandler(jwtService, objectMapper);
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new ErrorResponseAuthenticationFailureHandler(objectMapper);
  }

}
