package com.voodie.service;

import com.google.common.base.Preconditions;
import com.voodie.dao.UserDao;
import com.voodie.domain.Authorities;
import com.voodie.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserService {

	@PersistenceContext
	protected EntityManager em;

	@Inject
	protected UserDao userDao;

	public User create(String firstName, String lastName, String username, String password, String... roles) {
		Preconditions.checkNotNull(username);
		User existing = userDao.find(username);
		if (existing != null) {
			return null;
		} else {
			User newUser = new User();
			newUser.setUsername(username);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
			// TODO hash
			newUser.setPassword(password);
			newUser.setEnabled(true);
			em.persist(newUser);
			for (String r : roles) {
				Authorities a = new Authorities();
				a.setAuthority(r);
				a.setUser(newUser);
				em.persist(a);
			}
			return newUser;
		}
	}

	public Authorities findRole(String role) {
		Preconditions.checkNotNull(role);
		Authorities authority = userDao.findRole(role);
		return authority;
	}

	public User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth.isAuthenticated()) {
			return userDao.find(auth.getName());
		}
		return null;
	}
}
