package com.voodie.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.voodie.domain.FoodTruck;

@Stateless
public class FoodTruckDao {

	@PersistenceContext
	EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public FoodTruck find(String name, String externalId) {
		FoodTruck foodTruck = null;
		try {
			foodTruck = (FoodTruck) em
					.createQuery(
							"from FoodTruck where name = :name and externalId = :externalId")
					.setParameter("name", name)
					.setParameter("externalId", externalId).getSingleResult();
		} catch (NoResultException e) {
		}
		return foodTruck;
	}

	public FoodTruck find(String name) {
		FoodTruck foodTruck = null;
		try {
			foodTruck = (FoodTruck) em.createQuery(
					"from FoodTruck where name = :name").setParameter("name",
					name);
		} catch (NoResultException e) {
		}
		return foodTruck;
	}
}
