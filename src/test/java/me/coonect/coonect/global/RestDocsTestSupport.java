package me.coonect.coonect.global;

import static org.springframework.restdocs.snippet.Attributes.Attribute;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.coonect.coonect.common.security.configuration.SecurityConfiguration;
import me.coonect.coonect.global.config.RestDocsConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Disabled
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({RestDocsConfiguration.class, SecurityConfiguration.class})
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
      final RestDocumentationContextProvider provider) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
        .alwaysDo(MockMvcResultHandlers.print())
        .alwaysDo(restDocs)
        .build();
  }
}
