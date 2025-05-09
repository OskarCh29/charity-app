package pl.fundraising.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.fundraising.charity.entity.CharityAccount;

import java.util.List;

@Repository
public interface CharityAccountRepository extends JpaRepository<CharityAccount,Long> {

    @Query("SELECT fe.name, ca.balance, ca.currency_symbol" +
            "FROM charity_account ca " +
            "JOIN fundraising_event fe ")
    List<CharityAccount> getCharityFinancialReport();
}
