package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.Truck;

public interface TruckDao extends GenericDao<Truck> {

    /**
     * Find unoccupied by orders and not broken trucks by their cargo capacity.
     * 
     * @param minCargoCapacity
     * @return trucks or empty set
     */
    Set<Truck> findAvailiableTrucksByCapacity(float minCargoCapacity)
	    throws DaoException;

}
