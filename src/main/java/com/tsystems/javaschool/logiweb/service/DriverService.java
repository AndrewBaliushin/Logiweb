package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

/**
 * Data manipulation and business logic related to Drivers.
 * 
 * @author Andrey Baliushin
 */
public interface DriverService {
    
    Set<Driver> findAllDrivers() throws LogiwebServiceException;

    Driver findDriverById(int id) throws LogiwebServiceException;

    void editDriver(Driver editedDriver) throws LogiwebServiceException;

    void addDriver(Driver newDriver) throws LogiwebServiceException;
    
    void removeDriver(Driver driverToRemove) throws LogiwebServiceException;
    
    Set<Driver> findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(City city, float maxWorkingHours) throws LogiwebServiceException;
    
    float calculateWorkingHoursForDriver(Driver driver) throws LogiwebServiceException;
}
