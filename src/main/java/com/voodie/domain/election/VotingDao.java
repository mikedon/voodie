package com.voodie.domain.election;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class VotingDao {

	@PersistenceContext
	protected EntityManager em;


}
