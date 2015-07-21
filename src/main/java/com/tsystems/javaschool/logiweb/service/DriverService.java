package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.DriverShiftJournal;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.model.DriverModel;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

/**
 * Data manipulation and business logic related to Drivers.
 * 
 * @author Andrey Baliushin
 */
public interface DriverService {
    
    /**
     * Find drivers.
     * 
     * @return empty set if nothing found
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    Set<Driver> findAllDrivers() throws LogiwebServiceException;

    /**
     * Find driver by id.
     * 
     * @param id
     * @return truck or null
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    Driver findDriverById(int id) throws LogiwebServiceException;

    /**
     * Edit driver.
     * 
     * @param editedDriver as model
     * @throws ServiceValidationException
     *             if after edit driver don't have all required fields or have
     *             not unique employee ID
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    void editDriver(DriverModel editedDriver) throws ServiceValidationException, LogiwebServiceException;

    /**
     * Add Driver.
     * 
     * @param new Driver as model
     * @return id in db of created driver
     * @throws ServiceValidationException if driver don't have all required fields or have not unique employee ID
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    int addDriver(DriverModel newDriver) throws ServiceValidationException, LogiwebServiceException;
    
    /**
     * Remove driver.
     * 
     * @param driver
     * @throws ServiceValidationException if driver is attached to truck.
     * @throws LogiwebServiceException if unexpected exception on lower level occurred (not user fault)
     */
    void removeDriver(Driver driverToRemove) throws LogiwebServiceException;
    
    /**
     * Find drivers that are not assign to trucks.
     * find them by city and max working hours that they have in this month.
     * 
     * @param city
     * @param maxWorkingHours -- limit max amount of working hours that driver had in this month
     * @return drivers or empty set
     * @throws LogiwebServiceException if unexpected exception on lower level occurred (not user fault)
     */
    Set<Driver> findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(City city, float maxWorkingHours) throws LogiwebServiceException;
    
    /**
     * Calculate working hours for driver for this month.
     * 
     * Shift records that are started in previous month are trimmed. 
     * (Date of start is set to first day of the this month and 00:00 hours)
     * 
     * Records that don't have ending date (meaning that driver is currently on shift)
     * are also counted. End time for them is current time. 
     * 
     * @param driver
     * @return
     * @throws LogiwebServiceException if unexpected exception on lower level occurred (not user
     *             fault)
     */
    float calculateWorkingHoursForDriver(Driver driver) throws LogiwebServiceException;

    /**
     * Assign driver to truck.
     * 
     * @param driverId
     * @param truckId
     * @throws ServiceValidationException
     *             if truck or diver not exist, or if truck already have
     *             full crew assigned
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    void assignDriverToTruck(int driverId, int truckId) throws LogiwebServiceException;

    /**
     * Find shift records that are started or ended in this month. Records are
     * not trimmed. (Meaning that if record is started in previous month then it
     * will be show 'as is'.
     * 
     * @param driver
     * @return shift records or empty set
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    Set<DriverShiftJournal> findDriverJournalsForThisMonth(Driver driver) throws LogiwebServiceException;
}
