package pl.fundraising.charity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fundraising.charity.entity.CollectionBox;
import pl.fundraising.charity.entity.FundraisingEvent;
import pl.fundraising.charity.exception.BoxAlreadyAssignedException;
import pl.fundraising.charity.exception.DonationException;
import pl.fundraising.charity.exception.RecordNotFoundException;
import pl.fundraising.charity.model.request.DonationRequest;
import pl.fundraising.charity.model.response.CollectionBoxResponse;
import pl.fundraising.charity.repository.CollectionBoxRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {

    private final CollectionBoxRepository boxRepository;
    private final EventService eventService;
    private final DonationService donationService;
    private final AccountService accountService;
    private final CantorService cantorService;

    public CollectionBox createNewBox() {
        return boxRepository.save(new CollectionBox());
    }

    public List<CollectionBoxResponse> getAllBoxesInfo() {
        List<CollectionBox> allBoxes = boxRepository.findAll();

        return allBoxes
                .stream()
                .map(collectionBox -> new CollectionBoxResponse(
                        collectionBox.getId(),
                        collectionBox.isAssigned(),
                        collectionBox.isEmpty()
                )).toList();
    }

    public void assignBoxToEvent(long boxId, long eventId) {
        CollectionBox box = findById(boxId);
        if (box.isAssigned()) {
            throw new BoxAlreadyAssignedException("Box is already assigned to another fundraising event");
        }
        if (!box.isEmpty()) {
            donationService.deleteDonationFromBox(boxId);
        }
        FundraisingEvent event = eventService.findById(eventId);

        box.setFundraisingEvent(event);
        boxRepository.save(box);
    }

    public void donateBox(long boxId, DonationRequest request) {
        CollectionBox box = findById(boxId);

        if (!box.isAssigned()) {
            throw new DonationException("You cannot donate not assigned box");
        }
        CollectionBox updatedBox = donationService.calculateBoxValue(box, request);
        boxRepository.save(updatedBox);
    }

    public void transferFundsToAccount(long boxId) {
        CollectionBox box = findById(boxId);
        if(box.isEmpty() ){
            throw new DonationException("Cannot transfer funds because box is empty");
        }
        long assignedEventId = box.getFundraisingEvent().getId();
        String baseCurrency = accountService.checkAccountBaseCurrency(assignedEventId);

        BigDecimal payment = cantorService.exchangeBoxCurrencies(baseCurrency,box);

        accountService.receivePayment(payment,assignedEventId);
        donationService.deleteDonationFromBox(boxId);
    }

    public void deleteBox(Long boxId) {
        findById(boxId);
        boxRepository.deleteById(boxId);
    }

    public CollectionBox findById(long boxId) {
        return boxRepository.findById(boxId).orElseThrow(
                () -> new RecordNotFoundException("Box with id:" + boxId + " does not exist"));
    }

}
