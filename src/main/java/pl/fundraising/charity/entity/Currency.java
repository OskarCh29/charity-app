package pl.fundraising.charity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Currency {

    @Id
    private String symbol;

}
