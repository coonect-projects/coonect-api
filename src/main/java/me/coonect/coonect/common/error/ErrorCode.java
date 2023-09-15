package me.coonect.coonect.common.error;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public enum ErrorCode {

  DUPLICATE(SC_CONFLICT, "B-001", "Duplicate Value"),
  NOT_FOUND(SC_NOT_FOUND, "B-002", "Entity Not Found"),

  // Member
  EMAIL_DUPLICATE(SC_CONFLICT, "M-001", "Duplicate Email Address"),
  NICKNAME_DUPLICATE(SC_CONFLICT, "M-002", "Duplicate Nickname");

  private final String code;
  private final String message;
  private final int status;

  ErrorCode(final int status, final String code, final String message) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
