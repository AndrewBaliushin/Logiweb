package com.tsystems.javaschool.logiweb.dao.jpa;

import org.springframework.stereotype.Component;

import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;

/**
 * CRUD operations for Cargo entity (JPA implementation).
 * 
 * @author Andrey Baliushin
 */
@Component
public class DeliveryOrderDaoJpa extends GenericDaoJpa<DeliveryOrder> implements
        DeliveryOrderDao {

}
