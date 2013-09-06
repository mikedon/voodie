package com.voodie.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.voodie.domain.Authorities;
import com.voodie.domain.User;

@Stateless
public class UserDao {

	@PersistenceContext
	EntityManager em;

	public User find(String username) {
		User user = null;
		try {
			user = (User) em
					.createQuery("from User where username = :username")
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
		}
		return user;
	}

	public Authorities findRole(String role) {
		Authorities authority = null;
		try {
			authority = (Authorities) em
					.createQuery(
							"from Authorities where authority = :authority")
					.setParameter("authority", role).getSingleResult();
		} catch (NoResultException e) {
		}
		return authority;
	}
}
