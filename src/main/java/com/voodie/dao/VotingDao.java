package com.voodie.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.voodie.domain.Location;
import com.voodie.domain.Vote;

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

	@SuppressWarnings("unchecked")
	public List<Vote> getVotes(String foodTruckId, Date eatingTime) {
		List<Vote> votes = em
				.createQuery(
						"select v from Vote v where v.foodTruckId = :foodTruckId and eatingTime = :eatingTime")
				.setParameter("foodTruckId", foodTruckId)
				.setParameter("eatingTime", eatingTime).getResultList();
		return votes;
	}

}
