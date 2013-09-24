package com.voodie.domain.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class VotingService {

	@PersistenceContext
	protected EntityManager em;

}
