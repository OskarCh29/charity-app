package pl.fundraising.charity.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import pl.fundraising.charity.client.MoneyExchangeClient;
import pl.fundraising.charity.entity.MoneyExchange;
import pl.fundraising.charity.entity.CollectionBox;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.entity.Donation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@SpringBootTest
public class MoneyExchangeServiceTest {

    @MockitoBean
    private MoneyExchangeClient client;

    @Autowired
    private MoneyExchangeService moneyExchangeService;

    @Test
    void shouldCalculateAllCurrenciesToBaseCurrencyBasedOnExchangeRate() {
        String baseCurrency = "PLN";
        CollectionBox box = new CollectionBox();
        Donation donation = new Donation();
        donation.setCurrency(new Currency("EUR"));
        donation.setAmount(BigDecimal.valueOf(100));
        donation.setCollectionBox(box);
        List<Donation> boxDonations = List.of(donation);
        box.setBoxMoney(boxDonations);

        MoneyExchange testMoneyExchange = new MoneyExchange();
        testMoneyExchange.setBaseCurrency(baseCurrency);
        Map<String,BigDecimal> exchangeRates = new HashMap<>();
        exchangeRates.put("EUR",BigDecimal.valueOf(0.25));
        ReflectionTestUtils.setField(testMoneyExchange,"changingRates",exchangeRates);

        when(client.getExchangeRates(any(), any())).thenReturn(testMoneyExchange);

        BigDecimal exchangedValue = moneyExchangeService.exchangeBoxCurrencies(baseCurrency, box);

        assertEquals(BigDecimal.valueOf(400).stripTrailingZeros(),exchangedValue.stripTrailingZeros());
    }
}
