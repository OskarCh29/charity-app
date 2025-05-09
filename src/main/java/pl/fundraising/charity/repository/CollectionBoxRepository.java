package pl.fundraising.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fundraising.charity.entity.CollectionBox;

@Repository
public interface CollectionBoxRepository extends JpaRepository<CollectionBox,Long> {
}
