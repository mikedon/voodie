package com.voodie.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.voodie.dao.LocationDao;
import com.voodie.dao.VotingDao;
import com.voodie.domain.Location;
import com.voodie.domain.Vote;

@Stateless
public class VotingService {

	@PersistenceContext
	protected EntityManager em;

	@Inject
	protected LocationDao locationDao;

	@Inject
	protected VotingDao votingDao;

	public List<Vote> getVotes(String foodTruckId, Date eatingTime) {
		return votingDao.getVotes(foodTruckId, eatingTime);
	}

	// TODO use radius around coordinates, .5 mile?
	public Number getNumberOfVotes(String foodTruckId, Date eatingTime,
			Double latitude, Double longitude) {
		Location location = locationDao.find(latitude, longitude);
		if (location == null) {
			return 0;
		} else {
			return votingDao
					.getNumberOfVotes(foodTruckId, eatingTime, location);
		}
	}

	// TODO round eating time down to nearest 30 minute mark
	public boolean vote(String foodTruckId, Date eatingTime, Double latitude,
			Double longitude) {
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
