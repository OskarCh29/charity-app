package pl.fundraising.charity.security;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class CurrencyConstraintValidator implements ConstraintValidator<ValidCurrency, String> {

    // To be corrected later
    private final Set<String> validCurrencySymbols = Set.of("USD", "EUR", "PLN");

    @Override
    public boolean isValid(String currencySymbol, ConstraintValidatorContext constraintValidatorContext) {
        if(currencySymbol.length() != 3){
            return false;
        }
        return validCurrencySymbols.contains(currencySymbol);
    }

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {

    }
}
