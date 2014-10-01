package com.voodie.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Voodie
 * User: MikeD
 */
public abstract class AbstractDao<T> {

    @PersistenceContext
    protected EntityManager em;
}
