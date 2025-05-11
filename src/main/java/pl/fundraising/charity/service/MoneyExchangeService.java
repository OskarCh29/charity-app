package pl.fundraising.charity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fundraising.charity.client.MoneyExchangeClient;
import pl.fundraising.charity.entity.MoneyExchange;
import pl.fundraising.charity.entity.CollectionBox;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.entity.Donation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoneyExchangeService {
    private final MoneyExchangeClient client;

    public BigDecimal exchangeBoxCurrencies(String baseCurrency, CollectionBox charityBox) {
        List<Currency> boxMoneyCurrencies = charityBox.getBoxMoney().stream().map(Donation::getCurrency).toList();
        MoneyExchange moneyExchange = client.getExchangeRates(baseCurrency, boxMoneyCurrencies);

        List<Donation> donations = charityBox.getBoxMoney();

        return donations.stream()
                .map(donation -> {
                    BigDecimal rate = moneyExchange.getChangingRates().get(donation.getCurrency().getSymbol());
                    return donation.getAmount().divide(rate, 2, RoundingMode.HALF_UP);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
