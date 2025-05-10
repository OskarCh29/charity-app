package pl.fundraising.charity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fundraising.charity.client.CantorClient;
import pl.fundraising.charity.entity.Cantor;
import pl.fundraising.charity.entity.CollectionBox;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.entity.Donation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CantorService {
    private final CantorClient client;

    public BigDecimal exchangeBoxCurrencies(String baseCurrency, CollectionBox charityBox) {
        List<Currency> boxMoneyCurrencies = charityBox.getBoxMoney().stream().map(Donation::getCurrency).toList();
        Cantor cantor = client.getExchangeRates(baseCurrency, boxMoneyCurrencies);

        List<Donation> donations = charityBox.getBoxMoney();

        return donations.stream()
                .map(donation -> {
                    BigDecimal rate = cantor.getChangingRates().get(donation.getCurrency().getSymbol());
                    return donation.getAmount().divide(rate,2, RoundingMode.HALF_UP);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
