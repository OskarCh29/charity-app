package pl.fundraising.charity.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.fundraising.charity.entity.CollectionBox;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.entity.Donation;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class DonationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DonationRepository repository;

    @Test
    @DisplayName("Custom query in DonationRepository")
    void shouldDeleteAllDonationFromAssignedBox() {
        Currency currency = new Currency("USD");
        entityManager.persist(currency);

        CollectionBox box = new CollectionBox();
        entityManager.persist(box);

        Donation donation = new Donation();
        donation.setAmount(BigDecimal.valueOf(100));
        donation.setCurrency(currency);
        donation.setCollectionBox(box);

        entityManager.flush();

        repository.deleteAllBoxDonations(box.getId());

        assertEquals(0, repository.findAll().size());
    }

}
