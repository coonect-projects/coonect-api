package me.coonect.coonect.member.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import me.coonect.coonect.common.error.exception.ErrorCode;
import me.coonect.coonect.common.error.exception.NotFoundException;
import me.coonect.coonect.global.RestDocsTestSupport;
import me.coonect.coonect.member.adapter.in.web.dto.request.MemberSignupRequest;
import me.coonect.coonect.member.application.domain.exception.EmailDuplicationException;
import me.coonect.coonect.member.application.domain.exception.NicknameDuplicationException;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.in.MemberSignupUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(MemberSignupController.class)
class MemberSignupControllerTest extends RestDocsTestSupport {

  @MockBean
  private MemberSignupUseCase memberSignupUseCase;

  @Test
  public void signup_201() throws Exception {
    // given

    Member member = Member.withEncodedPassword(1L,
        "test@test.com",
        "encoded-password",
        "nickname",
        LocalDate.of(1995, 1, 10));

    given(memberSignupUseCase.signup(any())).willReturn(member);

    // when
    // then
    MemberSignupRequest request = new MemberSignupRequest(
        "test@test.com",
        "123456",
        "raw@password1",
        "nickname",
        LocalDate.of(1995, 1, 10));

    mockMvc.perform(post("/member")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.email").value("test@test.com"))
        .andExpect(jsonPath("$.nickname").value("nickname"))
        .andDo(restDocs.document(
            requestFields(
                fieldWithPath("email")
                    .type(STRING)
                    .description("멤버 이메일")
                    .attributes(constraints("이메일 형식")),
                fieldWithPath("nickname")
                    .type(STRING)
                    .description("멤버 닉네임")
                    .attributes(constraints("2자 이상 10자 이하 형식")),
                fieldWithPath("password")
                    .type(STRING)
                    .description("멤버 패스워드")
                    .attributes(constraints("8자 이상 20자 이하 최소 1글자 이상의 영어, 숫자, 특수문자 포함")),
                fieldWithPath("emailVerificationCode")
                    .type(STRING)
                    .description("이메일 인증 코드")
                    .attributes(constraints("6자리 숫자")),
                fieldWithPath("birthday")
                    .type(STRING)
                    .description("멤버 생년월일")
                    .attributes(constraints("YYYY-MM-DD"))
            ),
            responseFields(
                fieldWithPath("id")
                    .type(NUMBER)
                    .description("멤버 고유 번호"),
                fieldWithPath("email")
                    .type(STRING)
                    .description("멤버 이메일"),
                fieldWithPath("nickname")
                    .type(STRING)
                    .description("멤버 닉네임")
            )));

  }

  @Test
  public void signup_400_invalid_input() throws Exception {
    // given
    MemberSignupRequest request = new MemberSignupRequest(
        "test.com",
        "12345A",
        "!password2",
        "가가가",
        LocalDate.of(1995, 1, 10));

    // when
    // then

    mockMvc.perform(post("/member")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
  }

  @Test
  public void signup_409_nickname() throws Exception {
    // given
    MemberSignupRequest request = new MemberSignupRequest(
        "test@test.com",
        "123456",
        "raw@password1",
        "nickname",
        LocalDate.of(1995, 1, 10));

    willThrow(new NicknameDuplicationException("nickname"))
        .given(memberSignupUseCase).signup(any());

    // when
    // then
    mockMvc.perform(post("/member")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value(ErrorCode.NICKNAME_DUPLICATE.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.NICKNAME_DUPLICATE.getMessage()))
        .andExpect(jsonPath("$.reasons[0]").value("nickname"));
  }

  @Test
  public void signup_409_email() throws Exception {
    // given
    MemberSignupRequest request = new MemberSignupRequest(
        "test@test.com",
        "123456",
        "raw@password1",
        "nickname",
        LocalDate.of(1995, 1, 10));

    willThrow(new EmailDuplicationException("test@test.com"))
        .given(memberSignupUseCase).signup(any());

    // when
    // then
    mockMvc.perform(post("/member")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value(ErrorCode.EMAIL_DUPLICATE.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.EMAIL_DUPLICATE.getMessage()))
        .andExpect(jsonPath("$.reasons[0]").value("test@test.com"));
  }

  @Test
  public void signup_400_verified_email_not_found() throws Exception {
    // given
    MemberSignupRequest request = new MemberSignupRequest(
        "test@test.com",
        "123456",
        "raw@password1",
        "nickname",
        LocalDate.of(1995, 1, 10));

    willThrow(new NotFoundException(ErrorCode.VERIFIED_EMAIL_NOT_FOUND))
        .given(memberSignupUseCase).signup(any());

    // when
    // then
    mockMvc.perform(post("/member")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.VERIFIED_EMAIL_NOT_FOUND.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.VERIFIED_EMAIL_NOT_FOUND.getMessage()));
  }
}