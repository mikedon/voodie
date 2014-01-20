package com.voodie.domain.election;

import com.voodie.domain.AbstractJpaTest;
import com.voodie.domain.BuilderUtil;
import com.voodie.domain.foodie.Foodie;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Voodie
 * User: MikeD
 */
public class CheckInTest extends AbstractJpaTest {

    @Test
    public void testValidPersist(){
        CheckIn checkIn = new CheckIn();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        Election election = builderUtil.createElection(builderUtil.createFoodTruck());
        checkIn.setElection(election);
        Foodie foodie = builderUtil.createFoodie();
        checkIn.setFoodie(foodie);
        persistExpectingSuccess(checkIn);
    }

    @Test
    public void testPersistWithoutFoodie(){
        CheckIn checkIn = new CheckIn();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        Election election = builderUtil.createElection(builderUtil.createFoodTruck());
        checkIn.setElection(election);
        persistExpectingError(checkIn);
    }

    @Test
    public void testPersistWithoutElection(){
        CheckIn checkIn = new CheckIn();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        checkIn.setFoodie(builderUtil.createFoodie());
        persistExpectingError(checkIn);
    }

    @Test
    @Ignore("unique constraint not created for unit tests")
    public void testPersistWithDuplicate(){
        CheckIn checkIn = new CheckIn();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        Election election = builderUtil.createElection(builderUtil.createFoodTruck());
        checkIn.setElection(election);
        Foodie foodie = builderUtil.createFoodie();
        checkIn.setFoodie(foodie);
        persistExpectingSuccess(checkIn);

        CheckIn checkIn2 = new CheckIn();
        checkIn2.setElection(election);
        checkIn2.setFoodie(foodie);
        persistExpectingError(checkIn2);
    }
}
