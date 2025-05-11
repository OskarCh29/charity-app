package pl.fundraising.charity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import pl.fundraising.charity.entity.*;
import pl.fundraising.charity.exception.BoxAlreadyAssignedException;
import pl.fundraising.charity.exception.DonationException;
import pl.fundraising.charity.exception.RecordNotFoundException;
import pl.fundraising.charity.exception.TransferException;
import pl.fundraising.charity.model.request.DonationRequest;
import pl.fundraising.charity.model.response.CollectionBoxResponse;
import pl.fundraising.charity.repository.CollectionBoxRepository;
import pl.fundraising.charity.repository.CurrencyRepository;
import pl.fundraising.charity.repository.EventRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CollectionBoxServiceTest {

    @Autowired
    private CollectionBoxRepository boxRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CollectionBoxService boxService;

    @MockitoBean
    private DonationService donationService;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private MoneyExchangeService moneyExchangeService;

    @BeforeEach
    void clearRecords() {
        boxRepository.deleteAll();
        eventRepository.deleteAll();
        currencyRepository.deleteAll();
    }


    @Test
    void createNewBoxShouldCreateNewBoxEntity() {
        CollectionBox box = boxService.createNewBox();

        assertNotNull(box, "Created box should not be null");
        assertTrue(box.isEmpty());
        assertFalse(box.isAssigned());
        assertEquals(1, boxRepository.findAll().size());
    }

    @Test
    void getAllBoxesInfoShouldReturnBox() {
        createBoxTestCase();

        List<CollectionBoxResponse> boxStatus = boxService.getAllBoxesInfo();

        assertEquals(1, boxStatus.size());
        assertTrue(boxStatus.getFirst().isAssigned(), "Box should be assigned");
        assertTrue(boxStatus.getFirst().isEmpty());

    }

    @Test
    @Transactional
    void assignBoxToEventShouldAssignBoxToEventAndRemoveContents() {
        CollectionBox assignedBox = createBoxTestCase();

        CollectionBox newBox = new CollectionBox();
        Donation donation = new Donation();
        donation.setAmount(BigDecimal.TEN);
        donation.setCurrency(new Currency("PLN"));
        donation.setCollectionBox(newBox);
        newBox.getBoxMoney().add(donation);
        boxRepository.save(newBox);

        boxService.assignBoxToEvent(newBox.getId(), assignedBox.getFundraisingEvent().getId());

        CollectionBox updatedBox = boxRepository.findById(newBox.getId()).get();

        assertTrue(updatedBox.isAssigned());
    }
    @Test
    @Transactional
    void assignBoxToEventShouldAssignBoxToEvent() {
        CollectionBox assignedBox = createBoxTestCase();

        CollectionBox newBox = new CollectionBox();
        boxRepository.save(newBox);

        boxService.assignBoxToEvent(newBox.getId(), assignedBox.getFundraisingEvent().getId());

        CollectionBox updatedBox = boxRepository.findById(newBox.getId()).get();

        assertTrue(updatedBox.isAssigned());
    }

    @Test
    void assignBoxToEventBoxAlreadyAssigned() {
        CollectionBox assignedBox = createBoxTestCase();
        long eventId = 1;

        assertThrows(BoxAlreadyAssignedException.class, () -> boxService.assignBoxToEvent(assignedBox.getId(), eventId));

    }

    @Test
    @Transactional
    void unregisterBoxFromEventBoxEmpty() {
        CollectionBox testBox = createBoxTestCase();

        boxService.unregisterBoxFromEvent(testBox.getId());

        CollectionBox updatedBox = boxRepository.findById(testBox.getId()).get();

        assertFalse(updatedBox.isAssigned());

    }
    @Test
    @Transactional
    void unregisterBoxFromEventBoxNotEmpty() {
        CollectionBox testBox = createBoxTestCase();
        Donation donation = new Donation();
        donation.setAmount(BigDecimal.TEN);
        donation.setCurrency(new Currency("PLN"));
        donation.setCollectionBox(testBox);
        testBox.getBoxMoney().add(donation);
        boxRepository.save(testBox);

        boxService.unregisterBoxFromEvent(testBox.getId());

        CollectionBox updatedBox = boxRepository.findById(testBox.getId()).get();

        assertFalse(updatedBox.isAssigned());

    }

    @Test
    @Transactional
    void donateBoxShouldUpdateBoxBalance() {
        CollectionBox testBox = createBoxTestCase();
        DonationRequest request = new DonationRequest(BigDecimal.TEN,"PLN");

        Donation donation = new Donation();
        donation.setAmount(BigDecimal.TEN);
        donation.setCurrency(new Currency("PLN"));
        donation.setCollectionBox(testBox);
        testBox.getBoxMoney().add(donation);

        when(donationService.calculateBoxValue(any(), any())).thenReturn(testBox);
        boxService.donateBox(testBox.getId(),request);

        CollectionBox updatedBox = boxRepository.findById(testBox.getId()).get();
        List<Donation> donations = updatedBox.getBoxMoney();

        assertFalse(donations.isEmpty());
        assertEquals(BigDecimal.TEN, donations.getFirst().getAmount());
        assertEquals("PLN", donations.getFirst().getCurrency().getSymbol());

    }

    @Test
    void donateBoxShouldThrowExceptionBoxNotAssigned() {
        CollectionBox newBox = new CollectionBox();
        boxRepository.save(newBox);
        DonationRequest request = new DonationRequest(BigDecimal.TEN, "PLN");

        assertThrows(DonationException.class, () -> boxService.donateBox(newBox.getId(), request));
    }

    @Test
    @Transactional
    void transferFundsToAccountShouldTransferCorrectly(){
        CollectionBox box = createBoxTestCase();

        Donation donation = new Donation();
        donation.setAmount(BigDecimal.valueOf(100));
        donation.setCurrency(new Currency("PLN"));

        donation.setCollectionBox(box);
        box.getBoxMoney().add(donation);
        boxRepository.save(box);

        when(accountService.checkAccountBaseCurrency(box.getFundraisingEvent().getId())).thenReturn("USD");
        when(moneyExchangeService.exchangeBoxCurrencies("USD",box)).thenReturn(BigDecimal.valueOf(100));

        boxService.transferFundsToAccount(box.getId());

        verify(accountService).receivePayment(BigDecimal.valueOf(100), box.getFundraisingEvent().getId());
        verify(donationService).deleteDonationFromBox(box.getId());
    }


    @Test
    @Transactional
    void transferFundsToAccountShouldThrowExceptionBoxEmpty(){
        CollectionBox box = new CollectionBox();
        boxRepository.save(box);

        assertTrue(box.getBoxMoney().isEmpty());
        assertThrows(TransferException.class, () -> boxService.transferFundsToAccount(box.getId()));
    }


    @Test
    void getAllBoxesShouldThrowExceptionNoBoxesFound() {
        assertThrows(RecordNotFoundException.class, () -> boxService.getAllBoxesInfo(), "Database is empty");
    }

    @Test
    void deleteBoxShouldDeleteRecord() {
        CollectionBox box = new CollectionBox();
        boxRepository.save(box);

        boxService.deleteBox(box.getId());

        assertTrue(boxRepository.findAll().isEmpty(), "No boxes stored in DB");
    }


    @Test
    void findByIdShouldReturnBox() {
        CollectionBox box = new CollectionBox();
        boxRepository.save(box);

        CollectionBox foundBox = boxService.findById(box.getId());

        assertNotNull(foundBox, "Box should be found");
    }

    @Test
    void findByIdShouldThrowExceptionRecordNotFound() {
        long inValidId = 999;

        assertThrows(RecordNotFoundException.class, () -> boxService.findById(inValidId));
    }

    private CollectionBox createBoxTestCase() {
        Currency currency = new Currency("PLN");
        currencyRepository.save(currency);

        CharityAccount account = new CharityAccount();
        account.setCurrency(currency);
        account.setBalance(BigDecimal.valueOf(100));

        FundraisingEvent event = new FundraisingEvent();
        event.setName("TestEvent");
        account.setEvent(event);
        event.setAccount(account);

        CollectionBox testBox = new CollectionBox();

        event.setCollectionBoxes(List.of(testBox));
        testBox.setFundraisingEvent(event);

        eventRepository.save(event);

        return testBox;
    }


}
