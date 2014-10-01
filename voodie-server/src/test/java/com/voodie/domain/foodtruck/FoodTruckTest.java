package com.voodie.domain.foodtruck;

import com.voodie.domain.AbstractJpaTest;
import com.voodie.domain.BuilderUtil;
import com.voodie.domain.election.District;
import com.voodie.domain.identity.User;
import org.junit.Test;

/**
 * Voodie
 * User: MikeD
 */
public class FoodTruckTest extends AbstractJpaTest {

    @Test
    public void testPersistWithoutName(){
        FoodTruck foodTruck = new FoodTruck();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        User user = builderUtil.createUser();
        foodTruck.setUser(user);
        District district = builderUtil.createDistrict();
        foodTruck.setDistrict(district);
        persistExpectingError(foodTruck);
    }

    @Test
    public void testPersistWithoutUser(){
        FoodTruck foodTruck = new FoodTruck();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        District district = builderUtil.createDistrict();
        foodTruck.setDistrict(district);
        foodTruck.setName("foodtruck");
        persistExpectingError(foodTruck);
    }

    @Test
    public void testPersistWithoutDistrict(){
        FoodTruck foodTruck = new FoodTruck();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        User user = builderUtil.createUser();
        foodTruck.setUser(user);
        foodTruck.setName("foodtruck");
        persistExpectingError(foodTruck);
    }

    @Test
    public void testValidPersist(){
        FoodTruck foodTruck = new FoodTruck();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        User user = builderUtil.createUser();
        foodTruck.setUser(user);
        District district = builderUtil.createDistrict();
        foodTruck.setDistrict(district);
        foodTruck.setName("foodtruck");
        persistExpectingSuccess(foodTruck);
    }
}
