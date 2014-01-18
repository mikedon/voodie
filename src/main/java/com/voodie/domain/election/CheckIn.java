package com.voodie.domain.election;

import com.voodie.domain.foodie.Foodie;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Voodie
 * User: MikeD
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "foodie_id", "election_id" }))
public class CheckIn {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne
    @NotNull
    private Foodie foodie;

    @ManyToOne
    @NotNull
    private Election election;

    // ---------------------------------

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public Foodie getFoodie() {
        return foodie;
    }

    public void setFoodie(Foodie foodie) {
        this.foodie = foodie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
