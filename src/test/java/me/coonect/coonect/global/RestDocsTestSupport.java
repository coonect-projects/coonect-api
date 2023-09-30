package me.coonect.coonect.global;

import static org.springframework.restdocs.snippet.Attributes.Attribute;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import me.coonect.coonect.common.jwt.application.service.JwtProperties;
import me.coonect.coonect.common.jwt.application.service.JwtService;
import me.coonect.coonect.common.security.configuration.SecurityConfiguration;
import me.coonect.coonect.global.config.RestDocsConfiguration;
import me.coonect.coonect.mock.FakeMemberRepository;
import me.coonect.coonect.mock.FakeRefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.config.BeanIds;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

@Disabled
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({RestDocsConfiguration.class,
    SecurityConfiguration.class,
    FakeMemberRepository.class,
    JwtService.class, FakeRefreshTokenRepository.class, JwtProperties.class})
@ExtendWith(RestDocumentationExtension.class)
public class RestDocsTestSupport {

  @Autowired
  protected RestDocumentationResultHandler restDocs;
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;

  protected static Attribute constraints(
      final String value) {
    return new Attribute("constraints", value);
  }

  @BeforeEach
  void setUp(final WebApplicationContext context,
      final RestDocumentationContextProvider provider) throws ServletException {

    DelegatingFilterProxy delegateProxyFilter = new DelegatingFilterProxy();
    delegateProxyFilter.init(
        new MockFilterConfig(context.getServletContext(), BeanIds.SPRING_SECURITY_FILTER_CHAIN));

    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .addFilter(delegateProxyFilter)
        .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
        .alwaysDo(MockMvcResultHandlers.print())
        .alwaysDo(restDocs)
        .build();
  }
}
