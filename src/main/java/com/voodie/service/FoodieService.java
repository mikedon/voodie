package com.voodie.service;

import com.google.common.base.Preconditions;
import com.voodie.dao.FoodieDao;
import com.voodie.domain.FoodTruck;
import com.voodie.domain.Foodie;
import com.voodie.domain.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Voodie
 * User: MikeD
 */
public class FoodieService {

    @PersistenceContext
    protected EntityManager em;

    @Inject
    protected FoodieDao foodieDao;

    // ---------------------------------

    public boolean create(User user) {
        Foodie existing = foodieDao.findByUser(user.getUsername());
        if (existing != null) {
            return false;
        } else {
            Foodie newFoodie = new Foodie();
            newFoodie.setUser(user);
            newFoodie.setKarma(Foodie.DEFAULT_KARMA);
            em.persist(newFoodie);
            return true;
        }
    }

    public Foodie find(String username){
        Preconditions.checkNotNull(username);
        Foodie existing = foodieDao.findByUser(username);
        if(existing != null){
            return existing;
        }else{
            return null;
        }
    }
}
