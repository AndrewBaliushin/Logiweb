package com.tsystems.javaschool.logiweb.service.impl;

import javax.persistence.EntityManager;

public abstract class GenericServiceImpl {

    private EntityManager entityManager;
    
    public GenericServiceImpl(EntityManager em) {
	this.entityManager = em;
    }
    
    protected EntityManager getEntityManager() {
	return entityManager;
    }
    
    
}
