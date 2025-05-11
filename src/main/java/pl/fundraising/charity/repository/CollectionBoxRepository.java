package pl.fundraising.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.fundraising.charity.entity.CollectionBox;

import java.util.List;

@Repository
public interface CollectionBoxRepository extends JpaRepository<CollectionBox, Long> {

    @Query("SELECT DISTINCT box FROM CollectionBox box "
            + "LEFT JOIN FETCH box.boxMoney b")
    List<CollectionBox> findBoxesWithAllContent();

}
