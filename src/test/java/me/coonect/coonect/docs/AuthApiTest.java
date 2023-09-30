package me.coonect.coonect.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.coonect.coonect.common.error.exception.ErrorCode;
import me.coonect.coonect.global.RestDocsTestSupport;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest(excludeFilters = @ComponentScan.Filter(
    type = FilterType.ANNOTATION,
    classes = RestController.class
))
public class AuthApiTest extends RestDocsTestSupport {

  @MockBean
  private MemberRepository memberRepository;


  @Test
  public void login_200() throws Exception {
    // given
    String rawPassword = "raw@password1";

    Member member = Member.withEncodedPassword(1L,
        "test@test.com",
        PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(rawPassword),
        "nickname",
        LocalDate.of(1995, 1, 10));

    given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

    // when
    LoginRequest request = new LoginRequest("test@test.com", rawPassword);
    // then

    mockMvc.perform(post("/login")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("email")
                        .type(STRING)
                        .description("멤버 이메일"),
                    fieldWithPath("password")
                        .type(STRING)
                        .description("멤버 패스워드")
                ),
                responseFields(
                    fieldWithPath("tokenType")
                        .type(STRING)
                        .description("토큰 타입, Bearer로 고정"),
                    fieldWithPath("accessToken")
                        .type(STRING)
                        .description("이용자 엑세스 토큰 값"),
                    fieldWithPath("accessTokenExpiresIn")
                        .type(NUMBER)
                        .description("엑세스 토큰 만료 시간(초)"),
                    fieldWithPath("refreshToken")
                        .type(STRING)
                        .description("이용자 리프레시 토큰 값"),
                    fieldWithPath("refreshTokenExpiresIn")
                        .type(NUMBER)
                        .description("리프레시 토큰 만료 시간(초)")
                )
            )
        );
  }

  @Test
  public void login_401() throws Exception {
    // given
    given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

    LoginRequest request = new LoginRequest("test@test.com", "123123");
    // when
    // then

    mockMvc.perform(post("/login")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_USERNAME_OR_PASSWORD.getCode()))
        .andExpect(
            jsonPath("$.message").value(ErrorCode.INVALID_USERNAME_OR_PASSWORD.getMessage()));
  }

  @Getter
  @AllArgsConstructor
  private class LoginRequest {

    private String email;
    private String password;
  }
}
