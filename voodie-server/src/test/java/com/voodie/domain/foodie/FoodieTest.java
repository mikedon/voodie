package com.voodie.domain.foodie;

import com.voodie.domain.AbstractJpaTest;
import com.voodie.domain.BuilderUtil;
import org.junit.Test;

/**
 * Voodie
 * User: MikeD
 */
public class FoodieTest extends AbstractJpaTest {

    @Test
    public void testPersistWithoutUser(){
        Foodie foodie = new Foodie();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        foodie.setHomeDistrict(builderUtil.createDistrict());
        persistExpectingError(foodie);
    }

    @Test
    public void testPersistWithoutHomeDistrict(){
        Foodie foodie = new Foodie();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        foodie.setUser(builderUtil.createUser());
        persistExpectingError(foodie);
    }

    @Test
    public void testValidPersist(){
        Foodie foodie = new Foodie();
        BuilderUtil builderUtil = new BuilderUtil(getEntityManager());
        foodie.setHomeDistrict(builderUtil.createDistrict());
        foodie.setUser(builderUtil.createUser());
        persistExpectingSuccess(foodie);
    }
}
