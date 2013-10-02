package com.voodie;

import com.google.common.collect.Lists;
import com.voodie.domain.election.District;
import com.voodie.domain.election.DistrictDao;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Voodie
 * User: MikeD
 */
@Startup
@Singleton
public class VoodieInit {

    private static List<String> districts = Lists.newArrayList("Washington D.C.", "New York");

    @Inject
    protected DistrictDao districtDao;

    @PersistenceContext
    protected EntityManager em;

    // ---------------------------------

    @PostConstruct
    public void init(){
        for(String districtName : districts){
            if(districtDao.findDistrict(districtName) == null){
                District district = new District();
                district.setName(districtName);
                em.merge(district);
            }
        }
    }
}
