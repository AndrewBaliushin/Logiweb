package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

/**
 * Data manipulation and business logic related to Drivers.
 * 
 * @author Andrey Baliushin
 */
public interface DriverService {
    
    Set<Driver> findAllDrivers() throws LogiwebServiceException;

    Driver findDriverById(int id) throws LogiwebServiceException;

    void editDriver(Driver editedDriver) throws LogiwebServiceException;

    /**
     * Add Driver.
     * 
     * @param new Driver
     * @return same Driver
     * @throws ServiceValidationException if driver don't have all required fields or have not unique employee ID
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    Driver addDriver(Driver newDriver) throws ServiceValidationException, LogiwebServiceException;
    
    /**
     * Remove driver.
     * 
     * @param driver
     * @throws ServiceValidationException if driver is attached to truck.
     * @throws LogiwebServiceException if unexpected exception on lower level occurred (not user fault)
     */
    void removeDriver(Driver driverToRemove) throws LogiwebServiceException;
    
    Set<Driver> findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(City city, float maxWorkingHours) throws LogiwebServiceException;
    
    float calculateWorkingHoursForDriver(Driver driver) throws LogiwebServiceException;
}
