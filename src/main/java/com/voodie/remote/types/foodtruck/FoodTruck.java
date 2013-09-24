package com.voodie.remote.types.foodtruck;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FoodTruck implements Serializable {

	private String id;

    private String firstName;

    private String lastName;

    private String username;

	private String name;
	
	private List<String> categories = Lists.newArrayList();
	
	private String ratingImageUrl;
	
	private String url;
	
	private String imageUrl;

	private Long rating;

	private String address;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRatingImageUrl() {
		return ratingImageUrl;
	}

	public void setRatingImageUrl(String ratingImageUrl) {
		this.ratingImageUrl = ratingImageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}