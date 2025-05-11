package pl.fundraising.charity.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TrimmedNotBlankValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrimmedNotBlank {
    String message() default "Field too short or invalid format";

    int minLength() default 3;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
