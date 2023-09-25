package me.coonect.coonect.member.adapter.in.web;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.coonect.coonect.common.error.exception.BusinessException;
import me.coonect.coonect.common.error.exception.ErrorCode;
import me.coonect.coonect.global.RestDocsTestSupport;
import me.coonect.coonect.member.adapter.in.web.dto.request.EmailVerificationConfirmRequest;
import me.coonect.coonect.member.adapter.in.web.dto.request.SendVerificationMailRequest;
import me.coonect.coonect.member.application.domain.exception.EmailDuplicationException;
import me.coonect.coonect.member.application.port.in.EmailVerificationUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(EmailVerificationController.class)
class EmailVerificationControllerTest extends RestDocsTestSupport {

  @MockBean
  private EmailVerificationUseCase emailVerificationUseCase;

  @Test
  public void sendVerificationMail_201() throws Exception {
    // given
    willDoNothing().given(emailVerificationUseCase).sendVerificationEmail(any());

    // when
    SendVerificationMailRequest request = new SendVerificationMailRequest(
        "test@test.com");

    mockMvc.perform(post("/member/email/verification/request")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andDo(restDocs.document(
            requestFields(
                fieldWithPath("email")
                    .type(STRING)
                    .description("멤버 이메일")
                    .attributes(constraints("이메일 형식")))));

    // then
  }

  @Test
  public void sendVerificationMail_409() throws Exception {
    // given
    willThrow(new EmailDuplicationException("test@test.com"))
        .given(emailVerificationUseCase).sendVerificationEmail(any());

    // when
    SendVerificationMailRequest request = new SendVerificationMailRequest(
        "test@test.com");

    mockMvc.perform(post("/member/email/verification/request")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value(ErrorCode.EMAIL_DUPLICATE.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.EMAIL_DUPLICATE.getMessage()))
        .andExpect(jsonPath("$.reasons[0]").value("test@test.com"));
  }

  @Test
  public void sendVerificationMail_400() throws Exception {
    // given
    willThrow(new BusinessException(ErrorCode.MAIL_DELIVERY_ERROR.getMessage(),
        ErrorCode.MAIL_DELIVERY_ERROR))
        .given(emailVerificationUseCase).sendVerificationEmail(any());

    // when
    SendVerificationMailRequest request = new SendVerificationMailRequest(
        "test@test.com");

    mockMvc.perform(post("/member/email/verification/request")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.MAIL_DELIVERY_ERROR.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.MAIL_DELIVERY_ERROR.getMessage()))
        .andExpect(jsonPath("$.reasons").isEmpty());

  }

  @Test
  public void confirmEmailVerification_200() throws Exception {
    // given
    given(emailVerificationUseCase.verifyEmail(any())).willReturn(true);

    // when
    // then
    EmailVerificationConfirmRequest request = new EmailVerificationConfirmRequest(
        "test@test.com", "123456");

    mockMvc.perform(post("/member/email/verification/confirm")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestFields(
                fieldWithPath("email")
                    .type(STRING)
                    .description("멤버 이메일")
                    .attributes(constraints("이메일 형식")),
                fieldWithPath("code")
                    .type(STRING)
                    .description("이메일 인증 코드")
                    .attributes(constraints("6자리 숫자")))));

  }

  @Test
  public void confirmEmailVerification_404() throws Exception {
    // given
    given(emailVerificationUseCase.verifyEmail(any())).willReturn(false);

    // when
    // then
    EmailVerificationConfirmRequest request = new EmailVerificationConfirmRequest(
        "test@test.com", "123456");

    mockMvc.perform(post("/member/email/verification/confirm")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void confirmEmailVerification_400_validation_email() throws Exception {
    // given
    // when
    EmailVerificationConfirmRequest request = new EmailVerificationConfirmRequest(
        "test.com", "123456");

    // then
    mockMvc.perform(post("/member/email/verification/confirm")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
  }

  @Test
  public void confirmEmailVerification_400_validation_code() throws Exception {
    // given
    // when
    EmailVerificationConfirmRequest request = new EmailVerificationConfirmRequest(
        "test@test.com", "AAAAAA");

    // then
    mockMvc.perform(post("/member/email/verification/confirm")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
  }
}