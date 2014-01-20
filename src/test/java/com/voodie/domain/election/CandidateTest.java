package com.voodie.domain.election;

import com.voodie.domain.AbstractJpaTest;
import com.voodie.domain.BuilderUtil;
import org.junit.Test;

/**
 * Voodie
 * User: MikeD
 */
public class CandidateTest extends AbstractJpaTest {

    @Test
    public void testPersistWithoutDisplayName(){
        Candidate candidate = new Candidate();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        Election election = builderUtil.createElection(builderUtil.createFoodTruck());
        candidate.setElection(election);
        candidate.setLatitude(1d);
        candidate.setLongitude(1d);
        persistExpectingError(candidate);
    }

    @Test
    public void testPersistWithoutLongitude(){
        Candidate candidate = new Candidate();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        Election election = builderUtil.createElection(builderUtil.createFoodTruck());
        candidate.setElection(election);
        candidate.setLatitude(1d);
        candidate.setDisplayName("candidate");
        persistExpectingError(candidate);
    }

    @Test
    public void testPersistWithoutLatitude() {
        Candidate candidate = new Candidate();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        Election election = builderUtil.createElection(builderUtil.createFoodTruck());
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
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        Election election = builderUtil.createElection(builderUtil.createFoodTruck());
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
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        Election election = builderUtil.createElection(builderUtil.createFoodTruck());
        candidate.setElection(election);
        candidate.setLatitude(1d);
        candidate.setLongitude(1d);
        candidate.setDisplayName("candidate");
        persistExpectingSuccess(candidate);
    }
}
