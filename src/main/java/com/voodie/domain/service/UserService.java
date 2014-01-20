package com.voodie.domain.service;

import com.google.common.base.Preconditions;
import com.voodie.domain.identity.Authorities;
import com.voodie.domain.identity.User;
import com.voodie.domain.identity.UserDao;
import com.voodie.util.UserUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

	public User create(String firstName, String lastName, String username, String password, String emailAddress, String... roles) {
		Preconditions.checkNotNull(username);
		User existing = userDao.find(username);
		if (existing != null) {
			return null;
		} else {
			User newUser = new User();
			newUser.setUsername(username);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
			newUser.setPassword(UserUtil.encodePassword(password));
			newUser.setEnabled(true);
            newUser.setEmailAddress(emailAddress);
			em.persist(newUser);
			for (String r : roles) {
				Authorities a = findRole(r);
                if(a == null){
                    a = new Authorities();
                    a.setAuthority(r);
                    em.persist(a);
                }

                newUser.getAuthorities().add(a);
			}
            newUser = em.merge(newUser);
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

    public void autoLogin(User user){
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
