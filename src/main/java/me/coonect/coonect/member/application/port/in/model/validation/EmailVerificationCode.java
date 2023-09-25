package me.coonect.coonect.member.application.port.in.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import me.coonect.coonect.member.application.port.in.model.validation.validator.EmailVerificationCodeValidator;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailVerificationCodeValidator.class)
@Documented
public @interface EmailVerificationCode {

  String message() default "emailVerificationCode는 6자리 숫자로 이루어져야 합니다.";

  Class<?>[] groups() default {};

  Class<? extends String>[] payload() default {};
}
