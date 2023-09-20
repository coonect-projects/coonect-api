package me.coonect.coonect.member.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import me.coonect.coonect.member.adapter.in.web.NicknameValidationController;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import me.coonect.coonect.mock.TestContainer;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NicknameValidationControllerTest {

  private NicknameValidationController nicknameValidationController;
  private MemberRepository memberRepository;

  @Test
  public void 닉네임이_겹치면_409_응답을_내린다() throws Exception {
    // given
    TestContainer testContainer = TestContainer.builder().build();

    nicknameValidationController = testContainer.nicknameValidationController;
    memberRepository = testContainer.memberRepository;

    memberRepository.save(Member.withoutId("duk9741@gmail.com",
        "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10)));

    String nickname = "dukcode";

    // when
    ResponseEntity<Void> result = nicknameValidationController.validNickname(nickname);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(409));
    assertThat(result.getBody()).isNull();
  }

  @Test
  public void 닉네임이_겹치지_않으면_200_응답을_내린다() throws Exception {
    // given
    TestContainer testContainer = TestContainer.builder().build();

    nicknameValidationController = testContainer.nicknameValidationController;
    memberRepository = testContainer.memberRepository;

    memberRepository.save(Member.withoutId("duk9741@gmail.com",
        "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10)));

    String nickname = "newname";

    // when
    ResponseEntity<Void> result = nicknameValidationController.validNickname(nickname);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNull();
  }

}