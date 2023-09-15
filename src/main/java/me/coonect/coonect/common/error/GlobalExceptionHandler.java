package me.coonect.coonect.common.error;

import lombok.extern.slf4j.Slf4j;
import me.coonect.coonect.common.error.exception.BusinessException;
import me.coonect.coonect.common.error.exception.DuplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
    log.info("Handle BusinessException", e);
    final ErrorCode errorCode = e.getErrorCode();
    int status = errorCode.getStatus();

    return new ResponseEntity<>(ErrorResponse.of(errorCode), HttpStatusCode.valueOf(status));
  }

  @ExceptionHandler(DuplicationException.class)
  protected ResponseEntity<ErrorResponse> handleDuplicationException(final DuplicationException e) {
    log.info("Handle DuplicationException", e);

    final ErrorCode errorCode = e.getErrorCode();
    int status = errorCode.getStatus();
    String reason = e.getDuplicatedValue();

    return new ResponseEntity<>(ErrorResponse.of(errorCode, reason),
        HttpStatusCode.valueOf(status));
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Handle Exception", e);

    final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
