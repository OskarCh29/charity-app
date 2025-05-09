package pl.fundraising.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fundraising.charity.entity.FundraisingEvent;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<FundraisingEvent,Long> {
    Optional<FundraisingEvent> findByName(String name);
}
