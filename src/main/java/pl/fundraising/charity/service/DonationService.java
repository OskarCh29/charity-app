package pl.fundraising.charity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fundraising.charity.entity.CollectionBox;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.entity.Donation;
import pl.fundraising.charity.model.request.DonationRequest;
import pl.fundraising.charity.repository.DonationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;

    public void deleteDonationFromBox(long boxId) {
        donationRepository.deleteAllBoxDonations(boxId);
    }

    public CollectionBox calculateBoxValue(CollectionBox charityBox, DonationRequest request) {
        List<Donation> donations = charityBox.getBoxMoney();

        Optional<Donation> existingCurrency = donations.stream()
                .filter(donation -> donation.getCurrency().getSymbol().equals(request.getCurrency()))
                .findFirst();

        if (existingCurrency.isPresent()) {
            Donation found = existingCurrency.get();
            found.setAmount(found.getAmount().add(request.getAmount()));
            found.setCollectionBox(charityBox);
            donationRepository.save(found);
        } else {
            Donation donation = new Donation();
            donation.setAmount(request.getAmount());
            donation.setCurrency(new Currency(request.getCurrency()));
            donation.setCollectionBox(charityBox);

            donationRepository.save(donation);
        }
        return charityBox;
    }
}
