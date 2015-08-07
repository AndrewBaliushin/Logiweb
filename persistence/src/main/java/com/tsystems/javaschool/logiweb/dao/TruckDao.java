package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.Truck;

/**
 * CRUD operations for Truck entity.
 * 
 * @author Andrey Baliushin
 */
public interface TruckDao extends GenericDao<Truck> {

    /**
     * Find unoccupied by orders trucks with 'OK' status; find by their cargo capacity.
     * 
     * @param minCargoCapacity
     * @return trucks or empty set
     * @throws DaoException if something unexpected happened
     */
    Set<Truck> findByMinCapacityWhereStatusOkAndNotAssignedToOrder(
	    float minCargoCapacity)  throws DaoException;
    
    /**
     * Find truck by its License Plate number (unique)
     * 
     * @param licensePlate
     * @return null if nothing is found
     * @throws DaoException if something unexpected happened
     */
    Truck findByLicensePlate(String licensePlate) throws DaoException;

}
