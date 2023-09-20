package me.coonect.coonect.member.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import me.coonect.coonect.mock.TestContainer;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberSignupControllerTest {

  @Test
  public void 회원_가입에_성공하면_201_응답을_내린다() throws Exception {
    // given
    TestContainer testContainer = TestContainer.builder().build();

    MemberSignupController memberSignupController = testContainer.memberSignupController;

    MemberSignupRequest request = new MemberSignupRequest("duk9741@gmail.com",
        "!@#qwe123",
        "닉네임",
        LocalDate.of(1995, 1, 10));

    // when
    ResponseEntity<MemberResponse> result = memberSignupController.signup(request);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1L);
    assertThat(result.getBody().getEmail()).isEqualTo("duk9741@gmail.com");
    assertThat(result.getBody().getNickname()).isEqualTo("닉네임");
    // then

  }

}