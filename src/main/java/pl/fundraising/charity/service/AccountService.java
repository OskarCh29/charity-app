package pl.fundraising.charity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fundraising.charity.entity.CharityAccount;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.exception.RecordNotFoundException;
import pl.fundraising.charity.model.response.FinancialReportResponse;
import pl.fundraising.charity.repository.CharityAccountRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final CharityAccountRepository accountRepository;

    public CharityAccount createAccount(String baseCurrency) {
        CharityAccount account = new CharityAccount();
        Currency accountBaseCurrency = new Currency(baseCurrency);
        account.setCurrency(accountBaseCurrency);

        return accountRepository.save(account);
    }

    public void receivePayment(BigDecimal payment, long accountId) {
        CharityAccount account = findById(accountId);
        account.setBalance(account.getBalance().add(payment));
        accountRepository.save(account);

    }

    public List<FinancialReportResponse> getAccountsFinancialReport() {
        List<CharityAccount> allAccounts = accountRepository.findAllAccountsWithEventAndCurrency();
        if (allAccounts.isEmpty()) {
            throw new RecordNotFoundException("Cannot generate financial report - no fundraising event found");
        }

        return allAccounts.stream()
                .map(charityAccount -> new FinancialReportResponse(
                        charityAccount.getEvent().getName(),
                        charityAccount.getBalance(),
                        charityAccount.getCurrency().getSymbol())
                ).toList();
    }

    public String checkAccountBaseCurrency(long accountId) {
        CharityAccount account = findById(accountId);

        return account.getCurrency().getSymbol();
    }

    public CharityAccount findById(long accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new RecordNotFoundException("Account not exists"));
    }
}
