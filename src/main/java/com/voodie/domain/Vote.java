package com.voodie.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Vote {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Long id;

	public Date getEatingTime() {
		return eatingTime;
	}

	public void setEatingTime(Date eatingTime) {
		this.eatingTime = eatingTime;
	}

	private Date eatingTime;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	private Location location;

	public String getFoodTruckId() {
		return foodTruckId;
	}

	public void setFoodTruckId(String foodTruckId) {
		this.foodTruckId = foodTruckId;
	}

	private String foodTruckId;
}
