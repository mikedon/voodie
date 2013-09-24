package com.voodie.domain.service;

import com.google.common.base.Preconditions;
import com.voodie.domain.election.*;
import com.voodie.domain.foodie.Foodie;
import com.voodie.domain.foodtruck.FoodTruck;
import com.voodie.domain.foodtruck.FoodTruckDao;
import com.voodie.util.DateUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class ElectionService {

	@PersistenceContext
	protected EntityManager em;

	@Inject
	protected ElectionDao electionDao;

    @Inject
    protected VotingDao votingDao;

    @Inject
    protected FoodTruckDao foodTruckDao;

    // ---------------------------------

    public Vote vote(Foodie foodie, Candidate candidate){
        if(!hasFoodieAlreadyVoted(foodie, candidate)){
            Vote vote = new Vote();
            vote.setCandidate(candidate);
            vote.setFoodie(foodie);
            em.persist(vote);
            return vote;
        }
        return null;
    }

	public List<Election> getAllElectionsInProgress(Date pollOpeningDate, Date pollClosingDate) {
		return electionDao.findAllInProgress(pollOpeningDate, pollClosingDate);
	}

    public Election findElection(FoodTruck foodTruck, Date servingStartTime, Date servingEndTime){
        Preconditions.checkNotNull(servingStartTime);
        Preconditions.checkNotNull(servingEndTime);
        Election existing = electionDao.findInProgress(foodTruck, servingStartTime, servingEndTime);
        return existing;
    }

    public Election findElection(Long electionId){
        return em.find(Election.class, electionId);
    }

    public Candidate findCandidate(Long candidateId){
        return em.find(Candidate.class, candidateId);
    }

    public Election createElection(String username, String title, Date servingStartTime, Date servingEndTime, Date pollOpeningDate, Date pollClosingDate, List<Candidate> candidates, Boolean allowWriteIn){
        FoodTruck foodTruck = foodTruckDao.findByUser(username);
        if(findElection(foodTruck, servingStartTime, servingEndTime) == null){
            Election election = new Election();
            election.setServingStartTime(servingStartTime);
            election.setServingEndTime(servingEndTime);
            election.setPollClosingDate(DateUtil.truncateHours(pollClosingDate));
            election.setPollOpeningDate(DateUtil.truncateHours(pollOpeningDate));
            election.setTitle(title);
            election.setStatus(ElectionStatus.IN_PROGRESS);
            election.setCandidates(candidates);
            for(Candidate c : candidates){
                c.setElection(election);
            }
            election.setAllowWriteIn(allowWriteIn);
            election.setFoodTruck(foodTruck);
            em.persist(election);
            foodTruck.getElections().add(election);
            em.merge(foodTruck);
            return election;
        }
        return null;
    }

    protected boolean hasFoodieAlreadyVoted(Foodie foodie, Candidate candidate){
        Vote existing = null;
        for(Candidate c : candidate.getElection().getCandidates()){
            if(votingDao.findByUserAndCandidate(foodie, c) != null){
                return true;
            }
        }
        return false;
    }
}
