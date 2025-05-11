package pl.fundraising.charity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.fundraising.charity.entity.CharityAccount;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.entity.FundraisingEvent;
import pl.fundraising.charity.exception.EventAlreadyExistsException;
import pl.fundraising.charity.exception.RecordNotFoundException;
import pl.fundraising.charity.model.request.EventRequest;
import pl.fundraising.charity.repository.CurrencyRepository;
import pl.fundraising.charity.repository.EventRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @MockitoBean
    private AccountService accountService;

    @BeforeEach
    void cleanRecords(){
        eventRepository.deleteAll();
    }

    @Test
    void createEventRecordCreated() {
        EventRequest eventRequest = new EventRequest("NewCharity", "PLN");
        Currency currency = new Currency("PLN");
        currencyRepository.save(currency);
        CharityAccount account = new CharityAccount();
        account.setCurrency(currency);

        when(accountService.createAccount(any())).thenReturn(account);

        FundraisingEvent createdEvent = eventService.createEvent(eventRequest);

        assertNotNull(createdEvent, "Record should be created");
        assertEquals(eventRequest.getCharityName(), createdEvent.getName(),
                "Name of the event should be as request");
        assertEquals(eventRequest.getCurrencySymbol(), createdEvent.getAccount().getCurrency().getSymbol(),
                "Associated account should have the same currency");
    }

    @Test
    void createEventShouldThrowExceptionEventAlreadyExists() {
        initTestEvent();
        EventRequest eventRequest = new EventRequest("TestEvent", "PLN");

        assertThrows(EventAlreadyExistsException.class, () -> eventService.createEvent(eventRequest));
    }

    @Test
    void findByIdShouldReturnRecord() {
        FundraisingEvent existingEvent = initTestEvent();

        FundraisingEvent foundEvent = eventService.findById(existingEvent.getId());

        assertNotNull(foundEvent, "Event should be found");
        assertEquals(existingEvent.getName(), foundEvent.getName(), "Event names should be equal");

    }

    @Test
    void findByIdShouldThrowExceptionRecordNotFound() {
        long invalidId = 999;
        assertThrows(RecordNotFoundException.class, () -> eventService.findById(999));

    }

    private FundraisingEvent initTestEvent() {
        Currency currency = new Currency("PLN");
        currencyRepository.save(currency);

        CharityAccount account = new CharityAccount();
        account.setCurrency(currency);

        FundraisingEvent event = new FundraisingEvent();
        event.setName("TestEvent");

        account.setEvent(event);
        event.setAccount(account);

        return eventRepository.save(event);

    }
}
