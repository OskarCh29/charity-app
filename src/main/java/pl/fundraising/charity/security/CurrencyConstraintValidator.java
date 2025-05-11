package pl.fundraising.charity.security;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.repository.CurrencyRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrencyConstraintValidator implements ConstraintValidator<ValidCurrency, String> {

    private final CurrencyRepository currencyRepository;
    private static final int SYMBOL_LENGTH = 3;

    private Set<String> validCurrencySymbols;

    @Override
    public boolean isValid(String currencySymbol, ConstraintValidatorContext constraintValidatorContext) {
        if (currencySymbol == null || currencySymbol.length() != SYMBOL_LENGTH) {
            return false;
        }
        if (validCurrencySymbols == null) {
            validCurrencySymbols = currencyRepository.findAll()
                    .stream()
                    .map(Currency::getSymbol)
                    .collect(Collectors.toSet());
        }
        return validCurrencySymbols.contains(currencySymbol.toUpperCase());
    }

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
    }
}
