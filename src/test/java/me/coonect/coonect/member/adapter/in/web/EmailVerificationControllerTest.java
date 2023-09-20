package me.coonect.coonect.member.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import me.coonect.coonect.member.adapter.in.web.dto.request.EmailVerificationConfirmRequest;
import me.coonect.coonect.member.adapter.in.web.dto.request.SendVerificationMailRequest;
import me.coonect.coonect.member.application.port.out.persistence.EmailVerificationCodeRepository;
import me.coonect.coonect.mock.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EmailVerificationControllerTest {

  private EmailVerificationCodeRepository emailVerificationCodeRepository;

  private EmailVerificationController emailVerificationController;

  @BeforeEach
  public void init() {
    TestContainer testContainer = TestContainer.builder()
        .codeGenerator(() -> "123456")
        .exceptionEmail("exception@exception.com")
        .build();
    emailVerificationCodeRepository = testContainer.emailVerificationCodeRepository;

    emailVerificationController = testContainer.emailVerificationController;
  }

  @Test
  public void 이메일_인증_메일_전송이_성공하면_201_응답을_내린다() throws Exception {
    // given
    SendVerificationMailRequest request = new SendVerificationMailRequest(
        "duk9741@gmail.com");
    // when
    ResponseEntity<Void> result = emailVerificationController.sendVerificationMail(
        request);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
  }

  @Test
  public void 인증_번호_검증에_성공하면_200_응답을_내린다() throws Exception {
    // given
    String email = "duk9741@gmail.com";
    String code = "123456";
    emailVerificationCodeRepository.save(email, code, Duration.ZERO);
    EmailVerificationConfirmRequest request = new EmailVerificationConfirmRequest(
        email, code);

    // when
    ResponseEntity<Void> result = emailVerificationController.confirmEmailVerification(request);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
  }

  @Test
  public void 인증_번호_검증에_실패하면_404_응답을_내린다() throws Exception {
    // given
    String email = "duk9741@gmail.com";
    String code = "123456";
    String differentCode = "999999";

    emailVerificationCodeRepository.save(email, code, Duration.ZERO);
    EmailVerificationConfirmRequest request = new EmailVerificationConfirmRequest(
        email, differentCode);

    // when
    ResponseEntity<Void> result = emailVerificationController.confirmEmailVerification(request);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
  }
}