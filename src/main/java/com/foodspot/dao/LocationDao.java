package com.foodspot.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.foodspot.domain.Location;

@Stateless
public class LocationDao {

	@PersistenceContext
	EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Location find(Double latitude, Double longitude) {
		Location location = null;
		try {
			location = (Location) em
					.createQuery(
							"from Location where longitude = :long and latitude = :lat")
					.setParameter("long", longitude)
					.setParameter("lat", latitude).getSingleResult();
		} catch (NoResultException e) {
		}
		return location;
	}
}
