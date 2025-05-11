package pl.fundraising.charity.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DonationResponse {

    private long boxId;

    private BigDecimal amount;

    private String currency;

}
