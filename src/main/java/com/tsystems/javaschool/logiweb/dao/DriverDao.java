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
     * Find Drivers in specific city. Filter by maximum amount of
     * working hours in this month.
     * 
     * @param maxWorkingHours
     *            limit of working hours in this month
     * @param inCity
     *            city of interest
     * @return free drivers or empty set
     */
    Set<Driver> findByMaxWorkingHoursAndCityWhereNotAssignedToTruck(
            float maxWorkingHours, City inCity) throws DaoException;

    /**
     * Find driver by employee id.
     * 
     * @param id
     * @return Driver or null
     */
    Driver findByEmployeeId(int id) throws DaoException;

}
