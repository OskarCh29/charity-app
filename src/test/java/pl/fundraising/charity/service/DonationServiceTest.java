package pl.fundraising.charity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.fundraising.charity.entity.CollectionBox;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.entity.Donation;
import pl.fundraising.charity.model.request.DonationRequest;
import pl.fundraising.charity.repository.CollectionBoxRepository;
import pl.fundraising.charity.repository.CurrencyRepository;
import pl.fundraising.charity.repository.DonationRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DonationServiceTest {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CollectionBoxRepository boxRepository;

    @Autowired
    private DonationService donationService;

    @BeforeEach
    void clearRecords() {
        boxRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    @Test
    void deleteDonationFromTheBoxMakeBoxEmpty() {
        Currency currency = new Currency("USD");
        currencyRepository.save(currency);

        CollectionBox box = new CollectionBox();
        boxRepository.save(box);

        Donation donation = new Donation();
        donation.setAmount(BigDecimal.valueOf(100));
        donation.setCurrency(currency);
        donation.setCollectionBox(box);

        donationService.deleteDonationFromBox(box.getId());

        assertEquals(0, donationRepository.findAll().size());
    }

    @Test
    void calculateBoxValueBySummingDonations() {
        Currency currency = new Currency("PLN");
        currencyRepository.save(currency);

        CollectionBox box = new CollectionBox();


        Donation donation = new Donation();
        donation.setAmount(BigDecimal.valueOf(100));
        donation.setCurrency(currency);
        donation.setCollectionBox(box);
        box.setBoxMoney(List.of(donation));

        boxRepository.save(box);

        DonationRequest donationRequest = new DonationRequest(BigDecimal.TEN, "PLN");

        CollectionBox updatedBox = donationService.calculateBoxValue(box, donationRequest);

        assertEquals(BigDecimal.valueOf(110), updatedBox.getBoxMoney().getFirst().getAmount());

    }

    @Test
    void calculateBoxValueFirstDonation() {
        Currency currency = new Currency("PLN");
        Currency secondCurr = new Currency("USD");
        currencyRepository.save(currency);
        currencyRepository.save(secondCurr);

        CollectionBox box = new CollectionBox();


        Donation donation = new Donation();
        donation.setAmount(BigDecimal.valueOf(100));
        donation.setCurrency(currency);
        donation.setCollectionBox(box);
        box.getBoxMoney().add(donation);

        boxRepository.save(box);

        DonationRequest donationRequest = new DonationRequest(BigDecimal.TEN, "USD");

        CollectionBox updatedBox = donationService.calculateBoxValue(box, donationRequest);

        assertEquals(2, updatedBox.getBoxMoney().size());
        assertEquals(BigDecimal.valueOf(100), updatedBox.getBoxMoney().getFirst().getAmount());
        assertEquals(BigDecimal.valueOf(10), updatedBox.getBoxMoney().getLast().getAmount());

    }
}
