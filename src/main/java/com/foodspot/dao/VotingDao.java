package com.foodspot.dao;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.foodspot.domain.Location;

@Stateless
public class VotingDao {

	@PersistenceContext
	protected EntityManager em;

	public Number getNumberOfVotes(String foodTruckId, Date eatingTime,
			Location location) {
		Number count = (Number) em
				.createQuery(
						"select count(*) from Vote where eatingTime = :eatingTime and foodTruckId = :foodTruckId and location = :location")
				.setParameter("eatingTime", eatingTime)
				.setParameter("foodTruckId", foodTruckId)
				.setParameter("location", location).getSingleResult();
		return count;
	}

}
