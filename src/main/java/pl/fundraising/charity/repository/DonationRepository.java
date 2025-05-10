package pl.fundraising.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.fundraising.charity.entity.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Donation d "
            + "WHERE d.collectionBox.id = :boxId")
    void deleteAllBoxDonations(Long boxId);
}
