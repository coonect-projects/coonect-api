package me.coonect.coonect.common.restdocs.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
public class RestDocsConfiguration {

  @Profile("!prod")
  @Bean
  public WebSecurityCustomizer configureApiDocsEnable() {
    return web -> web.ignoring()
        .requestMatchers("/docs/**");
  }
}
