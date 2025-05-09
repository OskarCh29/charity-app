package pl.fundraising.charity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class CharityAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @ManyToOne
    private Currency currency;

    private BigDecimal balance = BigDecimal.ZERO;

    @OneToOne(mappedBy = "account")
    private FundraisingEvent event;

}
