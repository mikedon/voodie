package com.foodpsot.remote.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FoodTrucks implements Serializable {

	private List<FoodTruck> foodTrucks = Lists.newArrayList();

	public List<FoodTruck> getFoodTrucks() {
		return foodTrucks;
	}

	public void setFoodTrucks(List<FoodTruck> foodTrucks) {
		this.foodTrucks = foodTrucks;
	}
}