package com.voodie.domain.election;

import com.voodie.domain.AbstractJpaTest;
import com.voodie.domain.BuilderUtil;
import com.voodie.domain.foodtruck.FoodTruck;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

/**
 * Voodie
 * User: MikeD
 */
public class ElectionTest extends AbstractJpaTest {

    @Test
    public void testPersistWithoutTitle(){
        Election election = new Election();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        election.setFoodTruck(builderUtil.createFoodTruck());
        election.setPollOpeningDate(new Date());
        election.setPollClosingDate(new Date());
        election.setServingStartTime(new Date());
        election.setServingEndTime(new Date());
        election.setStatus(ElectionStatus.IN_PROGRESS);
        persistExpectingError(election);
    }

    @Test
    public void testPersistWithoutStatus(){
        Election election = new Election();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        election.setFoodTruck(builderUtil.createFoodTruck());
        election.setPollOpeningDate(new Date());
        election.setPollClosingDate(new Date());
        election.setServingStartTime(new Date());
        election.setServingEndTime(new Date());
        election.setTitle("title");
        persistExpectingError(election);
    }

    @Test
    public void testPersistWithoutPollOpeningDate(){
        Election election = new Election();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        election.setFoodTruck(builderUtil.createFoodTruck());
        election.setPollClosingDate(new Date());
        election.setServingStartTime(new Date());
        election.setServingEndTime(new Date());
        election.setStatus(ElectionStatus.IN_PROGRESS);
        election.setTitle("title");
        persistExpectingError(election);
    }

    @Test
    public void testPersistWithoutPollClosingDate(){
        Election election = new Election();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        election.setFoodTruck(builderUtil.createFoodTruck());
        election.setPollOpeningDate(new Date());
        election.setServingStartTime(new Date());
        election.setServingEndTime(new Date());
        election.setStatus(ElectionStatus.IN_PROGRESS);
        election.setTitle("title");
        persistExpectingError(election);
    }

    @Test
    public void testPersistWithoutServingStartTime(){
        Election election = new Election();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        election.setFoodTruck(builderUtil.createFoodTruck());
        election.setPollClosingDate(new Date());
        election.setPollOpeningDate(new Date());
        election.setServingEndTime(new Date());
        election.setStatus(ElectionStatus.IN_PROGRESS);
        election.setTitle("title");
        persistExpectingError(election);
    }

    @Test
    public void testPersistWithoutServingEndTime(){
        Election election = new Election();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        election.setFoodTruck(builderUtil.createFoodTruck());
        election.setPollClosingDate(new Date());
        election.setPollOpeningDate(new Date());
        election.setServingStartTime(new Date());
        election.setStatus(ElectionStatus.IN_PROGRESS);
        election.setTitle("title");
        persistExpectingError(election);
    }

    @Test
    public void testPersistWithoutFoodTruck(){
        Election election = new Election();
        election.setPollClosingDate(new Date());
        election.setPollOpeningDate(new Date());
        election.setServingEndTime(new Date());
        election.setServingStartTime(new Date());
        election.setStatus(ElectionStatus.IN_PROGRESS);
        election.setTitle("title");
        persistExpectingError(election);
    }

    @Test
    @Ignore("unique constraint not created for unit tests")
    public void testPersistWithDuplicate(){
        Election election = new Election();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        FoodTruck foodTruck = builderUtil.createFoodTruck();
        Date servingStartTime = new Date();
        Date servingEndTime = new Date();

        election.setFoodTruck(foodTruck);
        election.setPollClosingDate(new Date());
        election.setPollOpeningDate(new Date());
        election.setServingStartTime(servingStartTime);
        election.setServingEndTime(servingEndTime);
        election.setStatus(ElectionStatus.IN_PROGRESS);
        election.setTitle("title");
        persistExpectingSuccess(election);

        Election election2 = new Election();
        election2.setFoodTruck(foodTruck);
        election2.setPollClosingDate(new Date());
        election2.setPollOpeningDate(new Date());
        election2.setServingStartTime(servingStartTime);
        election2.setServingEndTime(servingEndTime);
        election2.setStatus(ElectionStatus.IN_PROGRESS);
        election2.setTitle("title");
        persistExpectingError(election);
    }

    @Test
    public void testValidPersist(){
        Election election = new Election();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        election.setFoodTruck(builderUtil.createFoodTruck());
        election.setPollClosingDate(new Date());
        election.setPollOpeningDate(new Date());
        election.setServingStartTime(new Date());
        election.setServingEndTime(new Date());
        election.setStatus(ElectionStatus.IN_PROGRESS);
        election.setTitle("title");
        persistExpectingSuccess(election);
    }
}
