package pl.fundraising.charity.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class FinancialReportResponse {

    private String charityName;

    private BigDecimal balance;

    private String currency;

}
