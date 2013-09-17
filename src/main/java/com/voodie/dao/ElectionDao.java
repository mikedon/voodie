package com.voodie.dao;

import com.voodie.domain.Election;
import com.voodie.domain.ElectionStatus;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import java.util.Date;

/**
 * Voodie
 * User: MikeD
 */
@Stateless
public class ElectionDao extends AbstractDao<Election> {

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Election findInProgress(Date servingStartTime, Date servingEndTime) {
        Election election = null;
        try {
            election = (Election) em
                    .createQuery(
                            "from Election where servingStartTime = :servingStartTime and servingEndTime = :servingEndTime and status = :status")
                    .setParameter("servingStartTime", servingStartTime)
                    .setParameter("servingEndTime", servingEndTime)
                    .setParameter("status", ElectionStatus.IN_PROGRESS).getSingleResult();
        } catch (NoResultException e) {
        }
        return election;
    }

}
