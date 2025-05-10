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
import pl.fundraising.charity.entity.Cantor;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.exception.CantorClientException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CantorClient {

    private final RestTemplate restTemplate;

    @Value("${cantor.baseUrl}")
    private String cantorUrl;

    @Value("${cantor.securityKey}")
    private String securityKey;

    public Cantor getExchangeRates(String baseCurrency, List<Currency> exchangeCurrencies) {
        try {
            String currencies = exchangeCurrencies.stream()
                    .map(Currency::getSymbol)
                    .collect(Collectors.joining(","));

            String urlFormat = cantorUrl + "?base_currency=" + baseCurrency + "&currencies=" + currencies;

            HttpHeaders headers = new HttpHeaders();
            headers.set("apiKey", securityKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Cantor> response = restTemplate.exchange(urlFormat, HttpMethod.GET, entity, Cantor.class);

            Cantor cantor = response.getBody();
            cantor.setBaseCurrency(baseCurrency);

            return cantor;

        } catch (HttpClientErrorException e) {
            throw new CantorClientException("Client error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }

}
