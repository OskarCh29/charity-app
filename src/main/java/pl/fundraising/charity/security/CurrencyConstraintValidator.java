package pl.fundraising.charity.security;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class CurrencyConstraintValidator implements ConstraintValidator<ValidCurrency, String> {

    private Set<String> validCurrencySymbols;

    @Override
    public boolean isValid(String currencySymbol, ConstraintValidatorContext constraintValidatorContext) {
        return validCurrencySymbols.contains(currencySymbol);
    }

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {

    }
}
