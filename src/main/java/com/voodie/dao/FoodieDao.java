package com.voodie.dao;

import com.voodie.domain.Foodie;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class FoodieDao {

	@PersistenceContext
	EntityManager em;

    public Foodie findByUser(String username){
        Foodie foodie = null;
        try {
            foodie = (Foodie) em
                    .createQuery("from Foodie where user.username = :username")
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
        }
        return foodie;
    }
}
