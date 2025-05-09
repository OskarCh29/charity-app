package pl.fundraising.charity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Currency {

    @Id
    private String symbol;

}
