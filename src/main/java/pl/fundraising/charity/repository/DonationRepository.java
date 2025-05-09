package pl.fundraising.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fundraising.charity.entity.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation,Long> {
}
