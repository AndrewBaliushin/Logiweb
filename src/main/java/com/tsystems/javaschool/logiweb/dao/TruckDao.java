package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.Truck;

public interface TruckDao extends GenericDao<Truck> {

    /**
     * Gets unoccupied by orders and not broken trucks by their 
     * cargo capacity.
     * 
     * @param minCargoCapacity
     * @return trucks
     */
    Set<Truck> getAvailiableTrucksByCapacity(Float minCargoCapacity);
 
}
