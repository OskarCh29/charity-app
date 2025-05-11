package pl.fundraising.charity.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.fundraising.charity.validator.TrimmedNotBlank;
import pl.fundraising.charity.validator.ValidCurrency;

@AllArgsConstructor
@Getter
public class EventRequest {

    @TrimmedNotBlank
    private String charityName;

    @ValidCurrency
    private String currencySymbol;
}
