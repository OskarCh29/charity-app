package pl.fundraising.charity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fundraising.charity.entity.CollectionBox;
import pl.fundraising.charity.entity.FundraisingEvent;
import pl.fundraising.charity.exception.BoxAlreadyAssignedException;
import pl.fundraising.charity.exception.DonationException;
import pl.fundraising.charity.exception.RecordNotFoundException;
import pl.fundraising.charity.model.request.DonationRequest;
import pl.fundraising.charity.model.response.CollectionBoxResponse;
import pl.fundraising.charity.repository.CollectionBoxRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {

    private final CollectionBoxRepository boxRepository;
    private final EventService eventService;
    private final DonationService donationService;

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
            donationService.deleteAllDonation(box.getBoxMoney());
            box.setBoxMoney(null);
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

    public void transferFundsToAccount(Long boxId) {

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
