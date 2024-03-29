package com.voodie.domain.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.voodie.domain.election.*;
import com.voodie.domain.foodie.Foodie;
import com.voodie.domain.foodtruck.FoodTruck;
import com.voodie.domain.foodtruck.FoodTruckDao;
import com.voodie.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
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

    @Inject
    protected DistrictDao districtDao;

    // ---------------------------------

    public List<District> getDistricts(){
        return districtDao.getDistricts();
    }

    public Election selectCandidate(Candidate candidate){
        Election election = candidate.getElection();
        election.setSelectedCandidated(candidate);
        election.setStatus(ElectionStatus.CLOSED);
        election = em.merge(election);
        return election;
    }

    public boolean addCandidate(Long electionId, Candidate candidate){
        Election election = findElection(electionId);
        if(election != null){
            try{
                candidate.setElection(election);
                em.persist(candidate);
                em.flush();
            }catch(PersistenceException e){
                return false;
            }
        }
        return true;
    }

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

	public List<Election> getAllElectionsInProgress(String district, Date servingStartTime, Date servingEndTime) {
        if(StringUtils.isEmpty(district)){
            return Lists.newArrayList();
        }
		return electionDao.findAllInProgress(district, servingStartTime, servingEndTime);
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
            if(candidates != null){
                election.setCandidates(candidates);
                for(Candidate c : candidates){
                    c.setElection(election);
                }
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

    public CheckIn createCheckIn(Foodie foodie, Election election){
        CheckIn checkIn = new CheckIn();
        checkIn.setFoodie(foodie);
        checkIn.setElection(election);
        em.persist(checkIn);
        return checkIn;
    }

    // ---------------------------------

    protected boolean hasFoodieAlreadyVoted(Foodie foodie, Candidate candidate){
        for(Candidate c : candidate.getElection().getCandidates()){
            if(votingDao.findByUserAndCandidate(foodie, c) != null){
                return true;
            }
        }
        return false;
    }
}
