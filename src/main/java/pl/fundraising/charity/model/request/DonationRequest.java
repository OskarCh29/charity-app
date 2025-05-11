package pl.fundraising.charity.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.fundraising.charity.validator.ValidCurrency;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DonationRequest {

    @NotNull(message = "Donation amount is required")
    @DecimalMin(value = "0.01", message = "Donation must be at least 0.01")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid monetary value")
    private BigDecimal amount;

    @ValidCurrency
    private String currency;

}
