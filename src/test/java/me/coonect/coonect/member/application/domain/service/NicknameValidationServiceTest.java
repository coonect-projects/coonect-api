package me.coonect.coonect.member.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.in.NicknameValidationQuery;
import me.coonect.coonect.member.application.port.in.NicknameValidationUseCase;
import me.coonect.coonect.member.application.port.out.MemberRepository;
import me.coonect.coonect.mock.FakeMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NicknameValidationServiceTest {

  private NicknameValidationUseCase nicknameValidationUseCase;

  @BeforeEach
  public void init() {
    MemberRepository memberRepository = new FakeMemberRepository();
    nicknameValidationUseCase = new NicknameValidationService(memberRepository);

    Member member = Member.withoutId("duk9741@gmail.com",
        "@12cdefghijkl",
        "dukcode",
        LocalDate.of(1995, 1, 10));

    memberRepository.save(member);
  }

  @Test
  public void 중복된_값을_요청하면_true_를_반환한다() throws Exception {
    // given
    String nickname = "dukcode";

    // when
    boolean result = nicknameValidationUseCase.isNicknameDuplicated(
        NicknameValidationQuery.of(nickname));

    // then
    assertThat(result).isTrue();
  }

  @Test
  public void 새로운_값을_요청하면_false_를_반환한다() throws Exception {
    // given
    String nickname = "xxxxxxx";

    // when
    boolean result = nicknameValidationUseCase.isNicknameDuplicated(
        NicknameValidationQuery.of(nickname));

    // then
    assertThat(result).isFalse();
  }
}