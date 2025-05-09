package pl.fundraising.charity.security;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TrimmedNotBlankValidator implements ConstraintValidator<TrimmedNotBlank, String> {

    private int minLength;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        String trimmed = value.trim();
        return !trimmed.isEmpty() && trimmed.length() >= minLength;
    }

    @Override
    public void initialize(TrimmedNotBlank constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
    }
}
