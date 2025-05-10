package pl.fundraising.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fundraising.charity.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {

}
