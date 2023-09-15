package me.coonect.coonect.common.error;

import static org.assertj.core.api.Assertions.assertThat;

import me.coonect.coonect.common.error.exception.ErrorCode;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ErrorResponseTest {

  @Test
  public void errorCode_로_ErrorResponse_를_생성할_수_있다() throws Exception {
    // given
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

    // when
    ErrorResponse errorResponse = ErrorResponse.of(errorCode);

    // then
    assertThat(errorResponse.getCode()).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
    assertThat(errorResponse.getMessage()).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    assertThat(errorResponse.getReasons()).isEmpty();
  }

  @Test
  public void errorCode_와_reason_으로_ErrorResponse_를_생성할_수_있다() throws Exception {
    // given
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

    // when
    ErrorResponse errorResponse = ErrorResponse.of(errorCode, "원인1", "원인2");

    // then
    assertThat(errorResponse.getCode()).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
    assertThat(errorResponse.getMessage()).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    assertThat(errorResponse.getReasons()).hasSize(2);
    assertThat(errorResponse.getReasons()).containsExactly("원인1", "원인2");
  }
}