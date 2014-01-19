package com.voodie.domain.foodtruck;

import com.voodie.domain.AbstractJpaTest;
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

        User user = new User();
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmailAddress("user@test.com");
        user.setPassword("password");
        user.setUsername("user");
        getEntityManager().persist(user);
        foodTruck.setUser(user);

        District district = new District();
        district.setName("district");
        getEntityManager().persist(district);
        foodTruck.setDistrict(district);

        persistExpectingError(foodTruck);
    }

    @Test
    public void testPersistWithoutUser(){
        FoodTruck foodTruck = new FoodTruck();
        District district = new District();
        district.setName("district2");
        getEntityManager().persist(district);
        foodTruck.setDistrict(district);

        foodTruck.setName("foodtruck");

        persistExpectingError(foodTruck);
    }

    @Test
    public void testPersistWithoutDistrict(){
        FoodTruck foodTruck = new FoodTruck();

        User user = new User();
        user.setFirstName("test2");
        user.setLastName("test2");
        user.setEmailAddress("user2@test.com");
        user.setPassword("password");
        user.setUsername("user2");
        getEntityManager().persist(user);
        foodTruck.setUser(user);

        foodTruck.setName("foodtruck");

        persistExpectingError(foodTruck);
    }

    @Test
    public void testValidPersist(){
        FoodTruck foodTruck = new FoodTruck();
        User user = new User();
        user.setFirstName("test3");
        user.setLastName("test3");
        user.setEmailAddress("user4@test.com");
        user.setPassword("password");
        user.setUsername("user3");
        getEntityManager().persist(user);
        foodTruck.setUser(user);

        District district = new District();
        district.setName("district3");
        getEntityManager().persist(district);
        foodTruck.setDistrict(district);

        foodTruck.setName("foodtruck");

        persistExpectingSuccess(foodTruck);
    }
}
