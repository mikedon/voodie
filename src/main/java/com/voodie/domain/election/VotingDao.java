package com.voodie.domain.election;

import com.voodie.domain.foodie.Foodie;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class VotingDao {

	@PersistenceContext
	protected EntityManager em;

    public Vote findByUserAndCandidate(Foodie foodie, Candidate candidate){
        Vote vote = null;
        String sql = "select v from Vote v where v.foodie = :foodie and v.candidate = :candidate";
        try{
        vote = (Vote)em.createQuery(sql)
            .setParameter("foodie", foodie)
            .setParameter("candidate", candidate)
            .getSingleResult();
        }catch(NoResultException e){
        }
        return vote;
    }

    public Long getNumberOfVotesForCandidate(Candidate candidate){
        String sql = "select count(*) from Vote where candidate = :candidate";
        return (Long) em.createQuery(sql)
                .setParameter("candidate", candidate)
                .getSingleResult();
    }

}
