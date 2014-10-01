package com.voodie.web;

import com.voodie.domain.identity.UserDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class VoodieUserDetailsService implements UserDetailsService {

	@Inject
	protected UserDao userDao;

    // ---------------------------------

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return userDao.find(username);
	}

}
