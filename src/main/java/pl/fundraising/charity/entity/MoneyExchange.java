package pl.fundraising.charity.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MoneyExchange {

    @Setter
    private String baseCurrency;

    @JsonProperty("data")
    private Map<String, BigDecimal> changingRates;

}
