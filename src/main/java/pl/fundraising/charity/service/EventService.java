package pl.fundraising.charity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fundraising.charity.entity.CharityAccount;
import pl.fundraising.charity.entity.FundraisingEvent;
import pl.fundraising.charity.exception.EventAlreadyExistsException;
import pl.fundraising.charity.model.request.EventRequest;
import pl.fundraising.charity.repository.EventRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AccountService accountService;

    public FundraisingEvent createEvent(EventRequest request) {
        Optional<FundraisingEvent> existingEvent = eventRepository.findByName(request.getCharityName());
        if (existingEvent.isPresent()) {
            throw new EventAlreadyExistsException("Event with provided name already exists");
        }
        CharityAccount charityAccount = accountService.createAccount(request.getCurrencySymbol());

        FundraisingEvent event = new FundraisingEvent();
        event.setName(request.getCharityName());
        event.setAccount(charityAccount);

        return eventRepository.save(event);

    }

}
