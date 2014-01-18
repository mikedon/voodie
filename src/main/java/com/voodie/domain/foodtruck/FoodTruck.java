package com.voodie.domain.foodtruck;

import com.google.common.collect.Lists;
import com.voodie.domain.election.District;
import com.voodie.domain.election.Election;
import com.voodie.domain.identity.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class FoodTruck {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Long id;

    @NotNull
	private String name;
	
	@OneToMany(mappedBy = "foodTruck")
	private List<Category> categories;

    @OneToMany(mappedBy = "foodTruck")
    private List<Election> elections;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private District district;

    // ---------------------------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getCategories() {
		if(categories == null){
			categories = Lists.newArrayList();
		}
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

    public List<Election> getElections() {
        return elections;
    }

    public void setElections(List<Election> elections) {
        this.elections = elections;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
