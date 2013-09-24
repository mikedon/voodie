package com.voodie.domain.election;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

}
