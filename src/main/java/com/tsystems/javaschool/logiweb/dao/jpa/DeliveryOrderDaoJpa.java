package com.tsystems.javaschool.logiweb.dao.jpa;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;

/**
 * CRUD operations for Cargo entity (JPA implementation).
 * 
 * @author Andrey Baliushin
 */
@Component
public class DeliveryOrderDaoJpa extends GenericDaoJpa<DeliveryOrder> implements
        DeliveryOrderDao {

}
