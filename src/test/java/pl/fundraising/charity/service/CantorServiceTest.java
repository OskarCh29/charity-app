package pl.fundraising.charity.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import pl.fundraising.charity.client.CantorClient;
import pl.fundraising.charity.entity.Cantor;
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
public class CantorServiceTest {

    @MockitoBean
    private CantorClient client;

    @Autowired
    private CantorService cantorService;

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

        Cantor testCantor = new Cantor();
        testCantor.setBaseCurrency(baseCurrency);
        Map<String,BigDecimal> exchangeRates = new HashMap<>();
        exchangeRates.put("EUR",BigDecimal.valueOf(0.25));
        ReflectionTestUtils.setField(testCantor,"changingRates",exchangeRates);

        when(client.getExchangeRates(any(), any())).thenReturn(testCantor);

        BigDecimal exchangedValue = cantorService.exchangeBoxCurrencies(baseCurrency, box);

        assertEquals(BigDecimal.valueOf(400).stripTrailingZeros(),exchangedValue.stripTrailingZeros());
    }
}
