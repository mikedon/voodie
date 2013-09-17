package com.voodie.service;

import com.google.common.base.Preconditions;
import com.voodie.dao.ElectionDao;
import com.voodie.dao.FoodTruckDao;
import com.voodie.domain.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless
public class FoodTruckService {

	@PersistenceContext
	protected EntityManager em;

	@Inject
	protected FoodTruckDao foodTruckDao;

    @Inject
    protected ElectionDao electionDao;

	public boolean create(User user, String foodTruckName) {
		Preconditions.checkNotNull(foodTruckName);
		FoodTruck existing = foodTruckDao.findByUser(user.getUsername());
		if (existing != null) {
			return false;
		} else {
			FoodTruck newFoodTruck = new FoodTruck();
            newFoodTruck.setUser(user);
			newFoodTruck.setName(foodTruckName);
			em.persist(newFoodTruck);
			return true;
		}
	}

    public FoodTruck find(String username){
        Preconditions.checkNotNull(username);
        FoodTruck existing = foodTruckDao.findByUser(username);
        if(existing != null){
            return existing;
        }else{
            return null;
        }
    }

    public Election findElection(FoodTruck foodTruck, Date servingStartTime, Date servingEndTime){
        Preconditions.checkNotNull(servingStartTime);
        Preconditions.checkNotNull(servingEndTime);
        Election existing = electionDao.findInProgress(foodTruck, servingStartTime, servingEndTime);
        return existing;
    }

    public List<Election> findAllElections(String username){
        Preconditions.checkNotNull(username);
        FoodTruck foodTruck = foodTruckDao.findByUser(username);
        if(foodTruck != null){
            return foodTruck.getElections();
        }
        return Collections.emptyList();
    }

    public Election createElection(String username, String title, Date servingStartTime, Date servingEndTime, Date pollOpeningDate, Date pollClosingDate, List<Candidate> candidates){
        FoodTruck foodTruck = find(username);
        if(findElection(foodTruck, servingStartTime, servingEndTime) == null){
            Election election = new Election();
            election.setServingStartTime(servingStartTime);
            election.setServingEndTime(servingEndTime);
            election.setPollClosingDate(pollClosingDate);
            election.setPollOpeningDate(pollOpeningDate);
            election.setTitle(title);
            election.setStatus(ElectionStatus.IN_PROGRESS);
            election.setCandidates(candidates);
            em.persist(election);
            foodTruck.getElections().add(election);
            em.merge(foodTruck);
            return election;
        }
        return null;
    }

}
