package com.foodspot.remote.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.google.common.collect.Lists;

@XmlSeeAlso(value = { FoodTruck.class })
@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FoodTrucks implements Serializable {

	private List<FoodTruck> foodTrucks = Lists.newArrayList();

	private Integer noOfResults;

	private Integer noOfPages;

	public List<FoodTruck> getFoodTrucks() {
		return foodTrucks;
	}

	public void setFoodTrucks(List<FoodTruck> foodTrucks) {
		this.foodTrucks = foodTrucks;
	}

	public Integer getNoOfResults() {
		return noOfResults;
	}

	public void setNoOfResults(Integer noOfResults) {
		this.noOfResults = noOfResults;
	}

	public Integer getNoOfPages() {
		return noOfPages;
	}

	public void setNoOfPages(Integer noOfPages) {
		this.noOfPages = noOfPages;
	}

}