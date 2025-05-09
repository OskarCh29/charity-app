package pl.fundraising.charity.security;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface ValidCurrency {

    String message () default "Invalid currency symbol";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
