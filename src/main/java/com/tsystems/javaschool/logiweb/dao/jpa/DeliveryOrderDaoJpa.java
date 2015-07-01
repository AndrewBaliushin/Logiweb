package com.tsystems.javaschool.logiweb.dao.jpa;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;

/**
 * CRUD operations for Cargo entity (JPA implementation).
 * 
 * @author Andrey Baliushin
 */
public class DeliveryOrderDaoJpa extends GenericDaoJpa<DeliveryOrder> implements
        DeliveryOrderDao {

    public DeliveryOrderDaoJpa(Class<DeliveryOrder> entityClass,
            EntityManager entityManager) {
	super(entityClass, entityManager);
    }

}
