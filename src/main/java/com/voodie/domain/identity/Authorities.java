package com.voodie.domain.identity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "user",
		"authority" }))
public class Authorities implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	public static final String FOOD_TRUCK = "Food Truck";
    public static final String FOODIE = "Foodie";

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user")
	private User user;

	@NotNull
	private String authority;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
