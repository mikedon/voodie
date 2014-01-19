package com.voodie.domain.election;

import com.voodie.domain.AbstractJpaTest;
import com.voodie.domain.foodtruck.FoodTruck;
import com.voodie.domain.identity.User;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import java.util.Date;

/**
 * Voodie
 * User: MikeD
 */
public class CandidateTest extends AbstractJpaTest {

    @Test
    public void testPersistWithoutDisplayName(){
        Candidate candidate = new Candidate();
        Election election = createElection(createFoodTruck());
        candidate.setElection(election);
        candidate.setLatitude(1d);
        candidate.setLongitude(1d);
        persistExpectingError(candidate);
    }

    @Test
    public void testPersistWithoutLongitude(){
        Candidate candidate = new Candidate();
        Election election = createElection(createFoodTruck());
        candidate.setElection(election);
        candidate.setLatitude(1d);
        candidate.setDisplayName("candidate");
        persistExpectingError(candidate);
    }

    @Test
    public void testPersistWithoutLatitude() {
        Candidate candidate = new Candidate();
        Election election = createElection(createFoodTruck());
        candidate.setElection(election);
        candidate.setLongitude(1d);
        candidate.setDisplayName("candidate");
        persistExpectingError(candidate);
    }

    @Test
    public void testPersistWithoutElection() {
        Candidate candidate = new Candidate();
        candidate.setLatitude(1d);
        candidate.setLongitude(1d);
        candidate.setDisplayName("candidate");
        persistExpectingError(candidate);
    }

    @Test
    public void testPersistWithDuplicate(){
        Candidate candidate = new Candidate();
        Election election = createElection(createFoodTruck());
        candidate.setElection(election);
        candidate.setLatitude(1d);
        candidate.setLongitude(1d);
        candidate.setDisplayName("candidate");
        persistExpectingSuccess(candidate);

        Candidate candidate2 = new Candidate();
        candidate2.setElection(election);
        candidate2.setLatitude(1d);
        candidate2.setLongitude(1d);
        candidate2.setDisplayName("candidate");
        persistExpectingError(candidate);
    }

    @Test
    public void testValidPersist(){
        Candidate candidate = new Candidate();
        Election election = createElection(createFoodTruck());
        candidate.setElection(election);
        candidate.setLatitude(1d);
        candidate.setLongitude(1d);
        candidate.setDisplayName("candidate");
        persistExpectingSuccess(candidate);
    }

    protected FoodTruck createFoodTruck(){
        FoodTruck foodTruck = new FoodTruck();
        User user = new User();
        user.setFirstName(RandomStringUtils.randomAlphanumeric(5));
        user.setLastName(RandomStringUtils.randomAlphanumeric(5));
        user.setEmailAddress(RandomStringUtils.randomAlphabetic(3).concat("@test.com"));
        user.setPassword("password");
        user.setUsername(RandomStringUtils.randomAlphanumeric(5));
        getEntityManager().persist(user);
        foodTruck.setUser(user);

        District district = new District();
        district.setName(RandomStringUtils.randomAlphabetic(5));
        getEntityManager().persist(district);
        foodTruck.setDistrict(district);

        foodTruck.setName(RandomStringUtils.randomAlphanumeric(5));

        persistExpectingSuccess(foodTruck);
        return foodTruck;
    }

    protected Election createElection(FoodTruck foodTruck){
        Election election = new Election();
        election.setStatus(ElectionStatus.IN_PROGRESS);
        election.setPollOpeningDate(new Date());
        election.setPollClosingDate(new Date());
        election.setServingStartTime(new Date());
        election.setServingEndTime(new Date());
        election.setFoodTruck(foodTruck);
        election.setTitle(RandomStringUtils.randomAlphabetic(5));
        persistExpectingSuccess(election);
        return election;
    }
}
