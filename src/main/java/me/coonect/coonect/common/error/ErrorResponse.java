package me.coonect.coonect.common.error;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {


  private String code;
  private String message;
  private List<String> reasons = new ArrayList<>();

  private ErrorResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }

  private ErrorResponse(String code, String message, List<String> reasons) {
    this.code = code;
    this.message = message;
    this.reasons = reasons;
  }

  static ErrorResponse of(ErrorCode errorCode) {
    return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
  }

  public static ErrorResponse of(ErrorCode errorCode, String... reasons) {
    return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), List.of(reasons));
  }
}
