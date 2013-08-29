package com.voodie.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.voodie.dao.UserDao;

@Stateless
public class VoodieUserDetailsService implements UserDetailsService {

	@Inject
	protected UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return userDao.find(username);
	}

}
