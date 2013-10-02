package com.voodie.domain.service;

import com.google.common.base.Preconditions;
import com.voodie.domain.election.District;
import com.voodie.domain.election.DistrictDao;
import com.voodie.domain.election.Election;
import com.voodie.domain.election.ElectionDao;
import com.voodie.domain.foodtruck.FoodTruck;
import com.voodie.domain.foodtruck.FoodTruckDao;
import com.voodie.domain.identity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Stateless
public class FoodTruckService {

	@PersistenceContext
	protected EntityManager em;

	@Inject
	protected FoodTruckDao foodTruckDao;

    @Inject
    protected ElectionDao electionDao;

    @Inject
    protected DistrictDao districtDao;

    // ---------------------------------

	public boolean create(User user, String foodTruckName, String districtName) {
		Preconditions.checkNotNull(foodTruckName);
		FoodTruck existing = foodTruckDao.findByUser(user.getUsername());
		if (existing != null) {
			return false;
		} else {
            District district = districtDao.findDistrict(districtName);
			FoodTruck newFoodTruck = new FoodTruck();
            newFoodTruck.setUser(user);
			newFoodTruck.setName(foodTruckName);
			newFoodTruck.setDistrict(district);
            em.persist(newFoodTruck);
			return true;
		}
	}

    public FoodTruck find(String username){
        Preconditions.checkNotNull(username);
        FoodTruck existing = foodTruckDao.findByUser(username);
        if(existing != null){
            return existing;
        }else{
            return null;
        }
    }


    public List<Election> findAllElections(String username){
        Preconditions.checkNotNull(username);
        FoodTruck foodTruck = foodTruckDao.findByUser(username);
        if(foodTruck != null){
            return foodTruck.getElections();
        }
        return Collections.emptyList();
    }

}
