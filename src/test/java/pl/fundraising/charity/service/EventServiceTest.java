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
import pl.fundraising.charity.repository.CharityAccountRepository;
import pl.fundraising.charity.repository.EventRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CharityAccountRepository accountRepository;

    @Autowired
    private EventService eventService;

    @MockitoBean
    private AccountService accountService;

    @BeforeEach
    void resetDb(){
        eventRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void createEventSavesNewRecord() {
        EventRequest request = new EventRequest("NewCharity", "PLN");

        CharityAccount account = new CharityAccount();
        account.setCurrency(new Currency("PLN"));
        accountRepository.save(account);

        when(accountService.createAccount(any())).thenReturn(account);

        FundraisingEvent newEvent = eventService.createEvent(request);

        assertEquals(request.getCharityName(), newEvent.getName(), "Events name should be equal");
        assertEquals(request.getCurrencySymbol(), newEvent.getAccount().getCurrency().getSymbol(),
                "When creating new event, account is automatically created and currency should be the same");

    }

    @Test
    void createEventShouldThrowEventAlreadyExistsException() {
        initTestCase();
        EventRequest eventRequest = new EventRequest("TestCharity", "PLN");

        assertThrows(EventAlreadyExistsException.class, () -> eventService.createEvent(eventRequest));
    }

    @Test
    void findByIdShouldReturnRecord() {
        FundraisingEvent event = initTestCase();

        FundraisingEvent found = eventService.findById(event.getId());

        assertNotNull(found, "Record should be found");
        assertEquals(event.getName(), found.getName());
    }

    @Test
    void findByIdShouldThrowExceptionRecordNotFound() {
        long invalidId = 132;

        assertThrows(RecordNotFoundException.class, () -> eventService.findById(invalidId));

    }

    private FundraisingEvent initTestCase() {
        FundraisingEvent event = new FundraisingEvent();
        event.setName("TestCharity");
        CharityAccount account = new CharityAccount();
        account.setCurrency(new Currency("USD"));
        event.setAccount(account);

        accountRepository.save(account);
        eventRepository.save(event);

        return event;
    }
}
