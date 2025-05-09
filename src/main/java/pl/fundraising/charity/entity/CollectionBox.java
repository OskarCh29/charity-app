package pl.fundraising.charity.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class CollectionBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private FundraisingEvent fundraisingEvent;

    @OneToMany(mappedBy = "collectionBox", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donation> boxMoney;


    public boolean isEmpty() {
        return boxMoney == null || boxMoney.isEmpty();
    }
}
