package pl.fundraising.charity.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class CharityAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Currency currency;

    private BigDecimal balance;

    @OneToOne(mappedBy = "account")
    private FundraisingEvent event;
}
