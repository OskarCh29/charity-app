package pl.fundraising.charity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class FundraisingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToOne
    private CharityAccount account;

    @OneToMany(mappedBy = "fundraisingEvent",cascade = CascadeType.ALL)
    private List<CollectionBox> collectionBoxes;
}
