package me.coonect.coonect.member.adapter.out.infrastructure;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@RequiredArgsConstructor
@Component
public class EmailVerificationHtmlLoaderImpl implements EmailVerificationHtmlLoader {

  private static final String TEMPLATE = "email-verification-template";
  private final SpringTemplateEngine templateEngine;

  @Override
  public String loadWith(String code) {
    HashMap<String, String> emailValues = new HashMap<>();
    emailValues.put("code", code);

    Context context = new Context();
    emailValues.forEach(context::setVariable);

    return templateEngine.process(TEMPLATE, context);
  }
}
