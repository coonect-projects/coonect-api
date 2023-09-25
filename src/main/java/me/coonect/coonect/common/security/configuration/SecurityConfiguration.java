package me.coonect.coonect.common.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

  private static final RequestMatcher[] ALLOWED_REQUESTS = {
      new AntPathRequestMatcher("/error", "GET"),
      new AntPathRequestMatcher("/member", "POST"),
      new AntPathRequestMatcher("/member/nickname/valid", "GET"),
      new AntPathRequestMatcher("/member/email/verification/request", "POST"),
      new AntPathRequestMatcher("/member/email/verification/confirm", "POST")
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(request -> request
            .requestMatchers(ALLOWED_REQUESTS).permitAll()
            .anyRequest().authenticated())
        .logout(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .requestCache(RequestCacheConfigurer::disable)
        .sessionManagement(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Profile("!prod")
  @Bean
  public WebSecurityCustomizer configureApiDocsEnable() {
    return web -> web.ignoring()
        .requestMatchers("/docs/**");
  }

}
