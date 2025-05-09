package pl.fundraising.charity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fundraising.charity.entity.CharityAccount;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.repository.CharityAccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private CharityAccountRepository accountRepository;

    public CharityAccount createAccount(String baseCurrency){
        CharityAccount account = new CharityAccount();
        Currency accountBaseCurrency = new Currency(baseCurrency);
        account.setCurrency(accountBaseCurrency);

        return accountRepository.save(account);
    }
}
