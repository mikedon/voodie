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
public class VoteTest extends AbstractJpaTest {

    @Test
    public void testWithoutCandidate(){
        Vote vote = new Vote();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        vote.setFoodie(builderUtil.createFoodie());
        persistExpectingError(vote);
    }

    @Test
    public void testWithoutFoodie(){
        Vote vote = new Vote();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        vote.setCandidate(builderUtil.createCandidate(builderUtil.createElection(builderUtil.createFoodTruck())));
        persistExpectingError(vote);
    }

    @Test
    @Ignore("unique constraint not created for unit tests")
    public void testPersistWithDuplicate(){
        Vote vote = new Vote();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        Candidate candidate = builderUtil.createCandidate(builderUtil.createElection(builderUtil.createFoodTruck()));
        Foodie foodie = builderUtil.createFoodie();
        vote.setCandidate(candidate);
        vote.setFoodie(foodie);
        persistExpectingSuccess(vote);

        Vote vote2 = new Vote();
        vote2.setCandidate(candidate);
        vote2.setFoodie(foodie);
        persistExpectingError(vote);
    }

    @Test
    public void testValidPersist(){
        Vote vote = new Vote();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        vote.setCandidate(builderUtil.createCandidate(builderUtil.createElection(builderUtil.createFoodTruck())));
        vote.setFoodie(builderUtil.createFoodie());
        persistExpectingSuccess(vote);
    }
}
