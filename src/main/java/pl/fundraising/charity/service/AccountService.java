package pl.fundraising.charity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fundraising.charity.entity.CharityAccount;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.model.response.FinancialReportResponse;
import pl.fundraising.charity.repository.CharityAccountRepository;

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

    public List<FinancialReportResponse> getAccountsFinancialReport(){
        List<CharityAccount> allAccounts = accountRepository.findAllAccountsWithEventAndCurrency();

        return allAccounts.stream()
                .map(charityAccount -> new FinancialReportResponse(
                        charityAccount.getEvent().getName(),
                        charityAccount.getBalance(),
                        charityAccount.getCurrency().getSymbol())
                ).toList();
    }
}
