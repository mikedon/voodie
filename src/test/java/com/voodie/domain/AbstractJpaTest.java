package com.voodie.domain;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Voodie
 * User: MikeD
 */
public class AbstractJpaTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager em;

    @BeforeClass
    public static void initTestFixture() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("voodie");
        em = entityManagerFactory.createEntityManager();
    }

    @AfterClass
    public static void closeTestFixture() {
        em.close();
        entityManagerFactory.close();
    }

    protected void persistExpectingError(Object entity){
        boolean invalid = false;
        try{
            getEntityManager().persist(entity);
        }catch(PersistenceException e){
            invalid = true;
        }
        assertTrue(invalid);
    }

    protected void persistExpectingSuccess(Object entity){
        boolean invalid = false;
        try{
            getEntityManager().persist(entity);
        }catch(PersistenceException e){
            invalid = true;
        }
        assertFalse(invalid);
    }

    protected EntityManager getEntityManager(){
        return em;
    }
}
