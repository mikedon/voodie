package com.voodie.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.google.common.base.Preconditions;
import com.voodie.dao.UserDao;
import com.voodie.domain.User;

@Stateless
public class UserService {

	@PersistenceContext
	protected EntityManager em;

	@Inject
	protected UserDao userDao;

	public boolean create(String username, String password) {
		Preconditions.checkNotNull(username);
		User existing = userDao.find(username);
		if (existing != null) {
			return false;
		} else {
			User newUser = new User();
			newUser.setUsername(username);
			// TODO hash
			newUser.setPassword(password);
			newUser.setEnabled(true);
			em.persist(newUser);
			return true;
		}
	}
}
