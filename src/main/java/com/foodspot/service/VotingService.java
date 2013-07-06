package com.foodspot.service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.foodspot.dao.LocationDao;
import com.foodspot.dao.VotingDao;
import com.foodspot.domain.Location;
import com.foodspot.domain.Vote;

@Stateless
public class VotingService {

	@PersistenceContext
	protected EntityManager em;

	@Inject
	protected LocationDao locationDao;

	@Inject
	protected VotingDao votingDao;

	public Number getNumberOfVotes(String foodTruckId, Date eatingTime,
			String latitude, String longitude) {
		Location location = locationDao.find(latitude, longitude);
		if (location == null) {
			return 0;
		} else {
			return votingDao
					.getNumberOfVotes(foodTruckId, eatingTime, location);
		}
	}

	public boolean vote(String foodTruckId, Date eatingTime, String latitude,
			String longitude) {
		Vote vote = new Vote();
		vote.setEatingTime(eatingTime);
		vote.setFoodTruckId(foodTruckId);
		Location location = locationDao.find(latitude, longitude);
		if (location == null) {
			location = new Location();
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			em.persist(location);
		}
		vote.setLocation(location);
		em.persist(vote);
		return true;
	}
}
