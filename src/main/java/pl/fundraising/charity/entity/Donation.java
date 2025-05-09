package pl.fundraising.charity.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Currency currency;

    private BigDecimal amount;

    @ManyToOne
    private CollectionBox collectionBox;
}
