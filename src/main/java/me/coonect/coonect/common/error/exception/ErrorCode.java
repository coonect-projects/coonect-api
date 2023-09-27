package me.coonect.coonect.common.error.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import lombok.Getter;

@Getter
public enum ErrorCode {

  // System
  INTERNAL_SERVER_ERROR(SC_INTERNAL_SERVER_ERROR, "S-001", "Internal Server Error"),

  // Common
  INVALID_TYPE_VALUE(SC_BAD_REQUEST, "C-001", "Invalid Type Value"),
  INVALID_INPUT_VALUE(SC_BAD_REQUEST, "C-002", "Invalid Input Value"),

  // Authentication
  INVALID_USERNAME_OR_PASSWORD(SC_UNAUTHORIZED, "A-001", "Invalid Username or Password"),

  // Business
  DUPLICATE(SC_CONFLICT, "B-001", "Duplicated Value"),
  NOT_FOUND(SC_NOT_FOUND, "B-002", "Entity Not Found"),
  MAIL_DELIVERY_ERROR(SC_BAD_REQUEST, "B-003", "Mail Delivery Error"),

  // Member
  EMAIL_DUPLICATE(SC_CONFLICT, "M-001", "Duplicate Email Address"),
  NICKNAME_DUPLICATE(SC_CONFLICT, "M-002", "Duplicate Nickname"),
  VERIFIED_EMAIL_NOT_FOUND(SC_NOT_FOUND, "M-003", "Verified Email Not Found");

  private final String code;
  private final String message;
  private final int status;

  ErrorCode(final int status, final String code, final String message) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
