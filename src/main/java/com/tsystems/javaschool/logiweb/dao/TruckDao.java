package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.Truck;

/**
 * CRUD operations for Truck entity.
 * 
 * @author Andrey Baliushin
 */
public interface TruckDao extends GenericDao<Truck> {

    /**
     * Find unoccupied by orders trucks with 'OK' status by their cargo capacity.
     * 
     * @param minCargoCapacity
     * @return trucks or empty set
     */
    Set<Truck> findByMinCapacityWhereStatusOkAndNotAssignedToOrder(
	    float minCargoCapacity)  throws DaoException;
    
    Truck findByLicensePlate(String licensePlate) throws DaoException;

}
