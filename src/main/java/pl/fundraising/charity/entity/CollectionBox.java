package pl.fundraising.charity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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

    public boolean isAssigned(){
        return fundraisingEvent == null;
    }
}
