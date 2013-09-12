package com.voodie.service;

import com.google.common.base.Preconditions;
import com.voodie.dao.FoodTruckDao;
import com.voodie.domain.FoodTruck;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class FoodTruckService {

	@PersistenceContext
	protected EntityManager em;

	@Inject
	protected FoodTruckDao foodTruckDao;

	public boolean create(String foodTruckName) {
		Preconditions.checkNotNull(foodTruckName);
		FoodTruck existing = foodTruckDao.find(foodTruckName);
		if (existing != null) {
			return false;
		} else {
			FoodTruck newFoodTruck = new FoodTruck();
			newFoodTruck.setName(foodTruckName);
			em.persist(newFoodTruck);
			return true;
		}
	}

    public FoodTruck find(String foodTruckName){
        Preconditions.checkNotNull(foodTruckName);
        FoodTruck existing = foodTruckDao.find(foodTruckName);
        if(existing != null){
            return existing;
        }else{
            return null;
        }
    }
}
