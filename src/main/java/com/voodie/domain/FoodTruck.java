package com.voodie.domain;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class FoodTruck {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Long id;

	private String externalId;

	@Expose
	private String name;
	
	@Expose
	private Long rating;
	
	private String ratingImageUrl;

	private String imageUrl;

	@Expose
	private String url;

	private String mobileUrl;

	private Long reviewCount;
	
	private String address;
	
	@OneToMany
	private List<Category> categories;

    @OneToMany
    private List<Election> elections;

    @ManyToOne
    private User user;

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

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMobileUrl() {
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	public Long getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Long reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
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
}
