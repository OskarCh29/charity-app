package pl.fundraising.charity.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import pl.fundraising.charity.entity.MoneyExchange;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.exception.MoneyExchangeClientException;
import pl.fundraising.charity.util.TestUtil;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoneyExchangeClientTest {

    private static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MoneyExchangeClient client;

    @BeforeAll
    public static void initOnce() {
        WIRE_MOCK_SERVER.start();
        WireMock.configureFor("localhost", WIRE_MOCK_SERVER.port());
    }

    @BeforeEach
    public void setup() {
        WIRE_MOCK_SERVER.resetAll();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("exchange.baseUrl", WIRE_MOCK_SERVER::baseUrl);
    }

    @Test
    void shouldReturnStatus200WithMappedMoneyExchangeObject() throws Exception {
        var mockResponse = TestUtil.getJsonFromFile("/response/exchange_status200_response.json");
        String baseCurrency = "PLN";


        WIRE_MOCK_SERVER.stubFor(WireMock.get(WireMock.urlPathMatching("/.*"))
                .withHeader("apiKey", WireMock.equalTo("secretKey"))
                .willReturn(ok()
                        .withBody(mockResponse)
                        .withHeader(CONTENT_TYPE, "application/json")));

        MoneyExchange moneyExchange = client.getExchangeRates(baseCurrency, List.of(new Currency("USD")));

        assertNotNull(moneyExchange, "Exchange rates should be obtained from API");
        assertEquals(baseCurrency, moneyExchange.getBaseCurrency(), "Rates should be according to base currency");
        assertEquals(BigDecimal.ONE, moneyExchange.getChangingRates().get("PLN"), "Exchange rate to base should 1:1");
        assertEquals(4, moneyExchange.getChangingRates().size(), "Rates should be provided for 4 basic curr");
    }

    @Test
    void shouldReturnStatus400WithErrorResponseMessage() {
        String baseCurrency = "PLN";
        List<Currency> currencyList = List.of(new Currency("USD"));
        WIRE_MOCK_SERVER.stubFor(WireMock.get(WireMock.urlPathMatching("/.*"))
                .willReturn(badRequest()));

        assertThrows(MoneyExchangeClientException.class, () -> client.getExchangeRates(baseCurrency, currencyList));
    }
}
