package me.coonect.coonect.member.application.port.in.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import me.coonect.coonect.member.application.port.in.model.validation.validator.PasswordValidator;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

  String message() default "password는 8자 이상, 20자 이상이어야 하고,"
      + " 최소 한 글자의 숫자, "
      + "최소 한 글자의 영문자, "
      + "최소 한 글자의 다음과 같은 특수문자(@, #, $, %, ^, &, +, =, 또는 !)를 포함해야 합니다.";

  Class<?>[] groups() default {};

  Class<? extends String>[] payload() default {};

}
