package me.coonect.coonect.member.adapter.in.web;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.coonect.coonect.global.RestDocsTestSupport;
import me.coonect.coonect.member.application.port.in.NicknameValidationUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(NicknameValidationController.class)
class NicknameValidationControllerTest extends RestDocsTestSupport {

  @MockBean
  private NicknameValidationUseCase nicknameValidationUseCase;

  @Test
  public void validateNickname_200() throws Exception {
    // given
    given(nicknameValidationUseCase.isNicknameDuplicated(any()))
        .willReturn(false);

    // when
    mockMvc.perform(get("/member/nickname/valid").queryParam("nickname", "dukcode"))
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            queryParameters(
                parameterWithName("nickname").description("유저 닉네임")
                    .attributes(constraints("8자 이상 20자 이하 최소 1글자 이상의 영어, 숫자, 특수문자 포함"))
            )
        ));

    // then
    verify(nicknameValidationUseCase).isNicknameDuplicated(any());
  }

  @Test
  public void validateNickname_400_validation() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/member/nickname/valid").queryParam("nickname", "ㄱ"))
        .andExpect(status().isBadRequest());

  }

  @Test
  public void validateNickname_409() throws Exception {
    // given
    given(nicknameValidationUseCase.isNicknameDuplicated(any()))
        .willReturn(true);

    // when
    mockMvc.perform(get("/member/nickname/valid").queryParam("nickname", "dukcode"))
        .andExpect(status().isConflict());

    // then
    verify(nicknameValidationUseCase).isNicknameDuplicated(any());
  }


}