package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.DriverStatus;

/**
 * Interface for basic operations for Driver entity.
 * 
 * @author Andrew Baliushin
 */
public interface DriverDao extends GenericDao<Driver> { 
    
    /**
     * Get Drivers in specific city with specific status.
     * Filter by amount of working hours that Drivers had in this month.
     * 
     * @param maxWorkingHours limit of working hours in this month
     * @param status status of driver
     * @param inCity city of interest
     * @return free drivers
     */
    Set<Driver> getAvailiable(float maxWorkingHours, City inCity, DriverStatus status);
}
