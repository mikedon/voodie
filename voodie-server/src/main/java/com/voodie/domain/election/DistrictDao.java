package com.voodie.domain.election;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DistrictDao {

	@PersistenceContext
	protected EntityManager em;

    @SuppressWarnings("unchecked")
    public List<District> getDistricts(){
        String sql = "select d from District d";
        return (List<District>) em.createQuery(sql).getResultList();
    }

    public District findDistrict(String name){
        String sql = "select d from District d where d.name = :name";
        try{
            return (District) em.createQuery(sql).setParameter("name", name).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
