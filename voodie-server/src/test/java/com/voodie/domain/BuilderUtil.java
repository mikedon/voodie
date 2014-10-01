package com.voodie.domain;

import com.voodie.domain.election.Candidate;
import com.voodie.domain.election.District;
import com.voodie.domain.election.Election;
import com.voodie.domain.election.ElectionStatus;
import com.voodie.domain.foodie.Foodie;
import com.voodie.domain.foodtruck.FoodTruck;
import com.voodie.domain.identity.User;
import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * Voodie
 * User: MikeD
 */
public class BuilderUtil {

    private EntityManager em;

    public BuilderUtil(EntityManager em){
        this.em = em;
    }

    public static BuilderUtil instance(EntityManager em){
        return new BuilderUtil(em);
    }

    public User createUser(){
        User user = new User();
        user.setFirstName(RandomStringUtils.randomAlphanumeric(5));
        user.setLastName(RandomStringUtils.randomAlphanumeric(5));
        user.setEmailAddress(RandomStringUtils.randomAlphabetic(3).concat("@test.com"));
        user.setPassword("password");
        user.setUsername(RandomStringUtils.randomAlphanumeric(5));
        em.persist(user);
        return user;
    }

    public District createDistrict(){
        District district = new District();
        district.setName(RandomStringUtils.randomAlphabetic(5));
        em.persist(district);
        return district;
    }

    public Foodie createFoodie(){
        Foodie foodie = new Foodie();
        foodie.setUser(createUser());
        foodie.setHomeDistrict(createDistrict());
        em.persist(foodie);
        return foodie;
    }

    public FoodTruck createFoodTruck(){
        FoodTruck foodTruck = new FoodTruck();
        User user = createUser();
        foodTruck.setUser(user);

        District district = createDistrict();
        foodTruck.setDistrict(district);

        foodTruck.setName(RandomStringUtils.randomAlphanumeric(5));

        em.persist(foodTruck);
        return foodTruck;
    }

    public Election createElection(FoodTruck foodTruck){
        Election election = new Election();
        election.setStatus(ElectionStatus.IN_PROGRESS);
        election.setPollOpeningDate(new Date());
        election.setPollClosingDate(new Date());
        election.setServingStartTime(new Date());
        election.setServingEndTime(new Date());
        election.setFoodTruck(foodTruck);
        election.setTitle(RandomStringUtils.randomAlphabetic(5));
        em.persist(election);
        return election;
    }

    public Candidate createCandidate(Election election){
        Candidate candidate = new Candidate();
        candidate.setDisplayName(RandomStringUtils.randomAlphanumeric(5));
        candidate.setLongitude(1d);
        candidate.setLatitude(1d);
        candidate.setElection(election);
        em.persist(candidate);
        return candidate;
    }
}
