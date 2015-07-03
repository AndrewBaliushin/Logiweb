package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;

/**
 * Interface for basic operations for Driver entity.
 * 
 * @author Andrey Baliushin
 */
public interface DriverDao extends GenericDao<Driver> {

    /**
     * Find driver by employee id.
     * 
     * @param id
     * @return Driver or null
     */
    Driver findByEmployeeId(int id) throws DaoException;
    
    /**
     * Find not assigned to truck drivers in the city.
     */
    Set<Driver> findByCityWhereNotAssignedToTruck(City city) throws DaoException;

}
