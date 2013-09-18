package com.voodie.service;

import com.voodie.dao.ElectionDao;
import com.voodie.domain.Election;

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

	public List<Election> getAllElectionsInProgress(Date pollOpeningDate, Date pollClosingDate) {
		return electionDao.findAllInProgress(pollOpeningDate, pollClosingDate);
	}

}
