package pl.fundraising.charity.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.fundraising.charity.entity.CharityAccount;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.entity.FundraisingEvent;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CharityAccountRepositoryTest {

    @Autowired
    private CharityAccountRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Custom query in AccountRepository")
    void shouldReturnAllAccountsWithEventAndCurrency(){
        FundraisingEvent event = new FundraisingEvent();
        event.setName("TestCharity");

        Currency currency = new Currency("USD");
        CharityAccount account = new CharityAccount();
        account.setCurrency(currency);
        account.setBalance(BigDecimal.valueOf(100));

        event.setAccount(account);
        account.setEvent(event);

        entityManager.persist(currency);
        entityManager.persist(account);
        entityManager.persist(event);
        entityManager.flush();

        List<CharityAccount> results = repository.findAllAccountsWithEventAndCurrency();

        assertEquals(1,results.size());
        CharityAccount result = results.getFirst();
        assertEquals("TestCharity",result.getEvent().getName());
        assertEquals("USD",result.getCurrency().getSymbol());
        assertEquals(BigDecimal.valueOf(100),result.getBalance());
    }
}
