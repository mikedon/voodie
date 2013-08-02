package com.voodie.remote.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Vote implements Serializable {

	private String foodTruckId;

	private Long eatingTime;

	private Double latitude;

	private Double longitude;

	public String getFoodTruckId() {
		return foodTruckId;
	}

	public void setFoodTruckId(String foodTruckId) {
		this.foodTruckId = foodTruckId;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Long getEatingTime() {
		return eatingTime;
	}

	public void setEatingTime(Long eatingTime) {
		this.eatingTime = eatingTime;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}