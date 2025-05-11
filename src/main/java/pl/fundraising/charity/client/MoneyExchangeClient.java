package pl.fundraising.charity.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.fundraising.charity.entity.MoneyExchange;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.exception.MoneyExchangeClientException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MoneyExchangeClient {

    private final RestTemplate restTemplate;

    @Value("${exchange.baseUrl}")
    private String exchangeUrl;

    @Value("${exchange.securityKey}")
    private String securityKey;

    public MoneyExchange getExchangeRates(String baseCurrency, List<Currency> exchangeCurrencies) {
        try {
            String currencies = exchangeCurrencies.stream()
                    .map(Currency::getSymbol)
                    .collect(Collectors.joining(","));

            StringBuilder queryBuilder = new StringBuilder()
                    .append(exchangeUrl)
                    .append("?base_currency=")
                    .append(baseCurrency)
                    .append("&currencies=")
                    .append(currencies);

            HttpHeaders headers = new HttpHeaders();
            headers.set("apiKey", securityKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<MoneyExchange> response = restTemplate.exchange(
                    queryBuilder.toString(), HttpMethod.GET, entity, MoneyExchange.class);

            MoneyExchange moneyExchange = response.getBody();
            moneyExchange.setBaseCurrency(baseCurrency);

            return moneyExchange;

        } catch (HttpClientErrorException e) {
            throw new MoneyExchangeClientException(
                    "Client error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }

}
