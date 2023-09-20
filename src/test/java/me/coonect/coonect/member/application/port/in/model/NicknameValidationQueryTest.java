package me.coonect.coonect.member.application.port.in.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import jakarta.validation.ConstraintViolationException;
import me.coonect.coonect.member.application.port.in.model.NicknameValidationQuery;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NicknameValidationQueryTest {

  @Test
  public void nickname_규칙을_지키면_생성할_수_있다() throws Exception {
    // given
    String nickname = "가나다asd";

    // when
    Throwable throwable = catchThrowable(() ->
        NicknameValidationQuery.of(nickname)
    );

    // then
    assertThat(throwable).isNull();

  }

  @Test
  public void nickname_규칙을_지키지_않으면_생성할_수_없다() throws Exception {
    // given
    String nickname = "가ㅏㅏ_jj";

    // when
    // then
    assertThatThrownBy(() ->
        NicknameValidationQuery.of(nickname))
        .isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("nickname");

  }

}