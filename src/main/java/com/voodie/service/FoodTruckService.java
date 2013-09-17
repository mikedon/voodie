package com.voodie.service;

import com.google.common.base.Preconditions;
import com.voodie.dao.ElectionDao;
import com.voodie.dao.FoodTruckDao;
import com.voodie.domain.Candidate;
import com.voodie.domain.Election;
import com.voodie.domain.FoodTruck;

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

	public boolean create(String foodTruckName) {
		Preconditions.checkNotNull(foodTruckName);
		FoodTruck existing = foodTruckDao.find(foodTruckName);
		if (existing != null) {
			return false;
		} else {
			FoodTruck newFoodTruck = new FoodTruck();
			newFoodTruck.setName(foodTruckName);
			em.persist(newFoodTruck);
			return true;
		}
	}

    public FoodTruck find(String foodTruckName){
        Preconditions.checkNotNull(foodTruckName);
        FoodTruck existing = foodTruckDao.find(foodTruckName);
        if(existing != null){
            return existing;
        }else{
            return null;
        }
    }

    public Election findElection(Date servingStartTime, Date servingEndTime){
        Preconditions.checkNotNull(servingStartTime);
        Preconditions.checkNotNull(servingEndTime);
        Election existing = electionDao.findInProgress(servingStartTime, servingEndTime);
        return existing;
    }

    public List<Election> findAllElections(String foodTruckId){
        Preconditions.checkNotNull(foodTruckId);
        FoodTruck foodTruck = foodTruckDao.find(foodTruckId);
        if(foodTruck != null){
            return foodTruck.getElections();
        }
        return Collections.emptyList();
    }

    public Election createElection(String title, Date servingStartTime, Date servingEndTime, Date pollOpeningDate, Date pollClosingDate, List<Candidate> candidates){
        if(findElection(servingStartTime, servingEndTime) == null){
            Election election = new Election();
            election.setServingStartTime(servingStartTime);
            election.setServingEndTime(servingEndTime);
            election.setPollClosingDate(pollClosingDate);
            election.setPollOpeningDate(pollOpeningDate);
            election.setTitle(title);
            election.setCandidates(candidates);
            em.persist(election);
            return election;
        }
        return null;
    }

}
