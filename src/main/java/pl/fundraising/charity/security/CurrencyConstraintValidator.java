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

    private Set<String> validCurrencySymbols;

    @Override
    public boolean isValid(String currencySymbol, ConstraintValidatorContext constraintValidatorContext) {
        if (currencySymbol == null || currencySymbol.length() != 3) {
            return false;
        }
        return validCurrencySymbols.contains(currencySymbol);
    }

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
        validCurrencySymbols = currencyRepository.findAll()
                .stream()
                .map(Currency::getSymbol)
                .collect(Collectors.toSet());
    }
}
