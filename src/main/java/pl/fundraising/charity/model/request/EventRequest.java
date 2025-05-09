package pl.fundraising.charity.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.fundraising.charity.security.ValidCurrency;

@AllArgsConstructor
@Getter
public class EventRequest {

    @NotBlank(message = "Charity event name cannot be empty")
    @Pattern(regexp = "^{3,}",message = "Event name should be at least 3 characters")
    private String charityName;

    @ValidCurrency
    private String currencySymbol;

}
