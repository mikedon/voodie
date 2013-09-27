package com.voodie.domain.election;

import com.voodie.domain.foodie.Foodie;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Voodie
 * User: MikeD
 */
@Entity
public class CheckIn {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne
    private Foodie foodie;

    @ManyToOne
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
