package com.voodie.domain.election;

import com.voodie.domain.foodie.Foodie;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne
    private Foodie foodie;

    @ManyToOne
    private Candidate candidate;

    // ---------------------------------

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public Foodie getFoodie() {
        return foodie;
    }

    public void setFoodie(Foodie foodie) {
        if(foodie != null){
            foodie.getVotes().add(this);
        }
        this.foodie = foodie;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        if(candidate != null){
            candidate.getVotes().add(this);
        }
        this.candidate = candidate;
    }
}
