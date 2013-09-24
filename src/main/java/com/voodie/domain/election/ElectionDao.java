package com.voodie.domain.election;

import com.voodie.domain.AbstractDao;
import com.voodie.domain.foodtruck.FoodTruck;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

/**
 * Voodie
 * User: MikeD
 */
@Stateless
public class ElectionDao extends AbstractDao<Election> {

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Election findInProgress(FoodTruck foodTruck, Date servingStartTime, Date servingEndTime) {
        Election election = null;
        try {
            election = (Election) em
                    .createQuery(
                            "from Election where foodTruck = :foodTruck and servingStartTime = :servingStartTime and servingEndTime = :servingEndTime and status = :status")
                    .setParameter("servingStartTime", servingStartTime)
                    .setParameter("servingEndTime", servingEndTime)
                    .setParameter("foodTruck", foodTruck)
                    .setParameter("status", ElectionStatus.IN_PROGRESS).getSingleResult();
        } catch (NoResultException e) {
        }
        return election;
    }

    @SuppressWarnings({"unchecked"})
    public List<Election> findAllInProgress(Date pollOpeningDate, Date pollClosingDate){
        return (List<Election>) em.createQuery("from Election where status = :status and pollOpeningDate <= :pollOpeningDate and pollClosingDate >= :pollClosingDate")
                .setParameter("status", ElectionStatus.IN_PROGRESS)
                .setParameter("pollOpeningDate", pollOpeningDate)
                .setParameter("pollClosingDate", pollClosingDate)
                .getResultList();
    }

}