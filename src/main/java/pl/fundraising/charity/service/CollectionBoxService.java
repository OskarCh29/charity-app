package pl.fundraising.charity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fundraising.charity.entity.CollectionBox;
import pl.fundraising.charity.exception.RecordNotFoundException;
import pl.fundraising.charity.model.response.CollectionBoxResponse;
import pl.fundraising.charity.repository.CollectionBoxRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {

    private final CollectionBoxRepository boxRepository;

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

    public void transferFundsToAccount(Long boxId) {

    }

    public void deleteBox(Long boxId) {
        if (!checkIfRecordExists(boxId)) {
            throw new RecordNotFoundException("Box which you want to unregister does not exist");
        }
        boxRepository.deleteById(boxId);
    }

    private boolean checkIfRecordExists(Long id) {
        Optional<CollectionBox> box = boxRepository.findById(id);
        return box.isPresent();
    }

}
