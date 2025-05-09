package pl.fundraising.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.fundraising.charity.entity.CharityAccount;

import java.util.List;

@Repository
public interface CharityAccountRepository extends JpaRepository<CharityAccount, Long> {

    @Query("SELECT ca FROM CharityAccount ca " +
            "JOIN FETCH ca.event e " +
            "JOIN FETCH ca.currency c")
    List<CharityAccount> findAllAccountsWithEventAndCurrency();
}
