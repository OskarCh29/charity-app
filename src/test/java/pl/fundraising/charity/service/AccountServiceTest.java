package pl.fundraising.charity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.fundraising.charity.entity.CharityAccount;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.entity.FundraisingEvent;
import pl.fundraising.charity.exception.RecordNotFoundException;
import pl.fundraising.charity.model.response.FinancialReportResponse;
import pl.fundraising.charity.repository.CharityAccountRepository;
import pl.fundraising.charity.repository.CurrencyRepository;
import pl.fundraising.charity.repository.EventRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private CharityAccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private EventRepository eventRepository;


    @BeforeEach
    void resetAccount() {
        accountRepository.deleteAll();
        eventRepository.deleteAll();
        currencyRepository.deleteAll();
    }


    @Test
    void createNewAccountShouldPostNewRecord() {
        Currency currency = new Currency("PLN");
        currencyRepository.save(currency);

        CharityAccount savedAccount = accountService.createAccount(currency.getSymbol());

        assertNotNull(savedAccount, "Record should not be null");
        assertEquals(currency.getSymbol(), savedAccount.getCurrency().getSymbol());
    }

    @Test
    void receivePaymentShouldAddMoneyToBalance() {
        CharityAccount account = initTestAccount();
        BigDecimal payment = BigDecimal.valueOf(100);

        accountService.receivePayment(payment, account.getId());
        CharityAccount updatedAccount = accountRepository.findById(account.getId()).get();

        assertEquals(BigDecimal.valueOf(200).stripTrailingZeros(), updatedAccount.getBalance().stripTrailingZeros());
    }

    @Test
    void getAccountFinancialReportShouldReturnResult() {
        initTestAccount();

        List<FinancialReportResponse> response = accountService.getAccountsFinancialReport();

        assertEquals(1,response.size());
        FinancialReportResponse result = response.getFirst();
        assertEquals("TestEvent",result.getCharityName());
        assertEquals("PLN",result.getCurrency());
        assertEquals(BigDecimal.valueOf(100).stripTrailingZeros(),result.getBalance().stripTrailingZeros());
    }


    @Test
    void getAccountsFinancialReportShouldThrowException() {
        assertThrows(RecordNotFoundException.class, () -> accountService.getAccountsFinancialReport(),
                "Database is empty, report cannot be generated");
    }


    @Test
    void findByIdShouldReturnTestAccount() {
        CharityAccount testAccount = initTestAccount();

        CharityAccount foundAccount = accountService.findById(testAccount.getId());

        assertNotNull(foundAccount, "Account Should not be null");
        assertEquals(testAccount.getBalance().stripTrailingZeros(), foundAccount.getBalance().stripTrailingZeros());
        assertEquals(testAccount.getCurrency().getSymbol(), foundAccount.getCurrency().getSymbol());
    }

    @Test
    void findByIdShouldThrowRecordNotFoundException() {
        long invalidId = 999;

        assertThrows(RecordNotFoundException.class, () -> accountService.findById(invalidId));
    }

    @Test
    void checkAccountBaseCurrencyShouldReturnCurrencySymbol() {
        CharityAccount testAccount = initTestAccount();

        String baseCurrencySymbol = accountService.checkAccountBaseCurrency(testAccount.getId());

        assertEquals(testAccount.getCurrency().getSymbol(), baseCurrencySymbol, "Symbols should be equal");
    }


    private CharityAccount initTestAccount() {
        Currency currency = new Currency("PLN");
        currencyRepository.save(currency);

        CharityAccount account = new CharityAccount();
        account.setCurrency(currency);
        account.setBalance(BigDecimal.valueOf(100));

        FundraisingEvent event = new FundraisingEvent();
        event.setName("TestEvent");

        account.setEvent(event);
        event.setAccount(account);
        eventRepository.save(event);

        return account;

    }
}
