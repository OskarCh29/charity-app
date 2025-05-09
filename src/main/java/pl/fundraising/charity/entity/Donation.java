package pl.fundraising.charity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
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
