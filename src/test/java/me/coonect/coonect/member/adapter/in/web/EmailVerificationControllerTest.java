package me.coonect.coonect.member.adapter.in.web;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.coonect.coonect.common.error.exception.BusinessException;
import me.coonect.coonect.common.error.exception.ErrorCode;
import me.coonect.coonect.global.RestDocsTestSupport;
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
        .andExpect(status().isCreated());

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

}