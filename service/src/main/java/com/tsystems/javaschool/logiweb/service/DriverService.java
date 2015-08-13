package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.DriverShiftJournal;
import com.tsystems.javaschool.logiweb.model.DriverModel;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException;
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
     * @throws LogiwebServiceException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    Set<DriverModel> findAllDrivers() throws LogiwebServiceException;

    /**
     * Find driver by id.
     * 
     * @param id
     * @return driver model or null
     * @throws LogiwebServiceException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    DriverModel findDriverById(int id) throws LogiwebServiceException;

    /**
     * Find driver by employee id.
     * 
     * @param employeeId
     * @return driver or null
     * @throws LogiwebServiceException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    Driver findDriverByEmployeeId(int employeeId)
            throws LogiwebServiceException;

    /**
     * Edit driver.
     * 
     * @param editedDriver
     *            as model
     * @throws ServiceValidationException
     *             if after edit driver don't have all required fields or have
     *             not unique employee ID or account name
     * @throws LogiwebServiceException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    void editDriverAndAccountName(DriverModel editedDriver,
            String newAccountName) throws ServiceValidationException,
            LogiwebServiceException;

    /**
     * Add driver and account for him.
     * 
     * @param new Driver as model
     * @return id in db of created driver
     * @throws ServiceValidationException
     *             if driver don't have all required fields or have not unique
     *             employee ID or account name is occupied
     * @throws LogiwebServiceException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    int addDriverWithAccount(DriverModel newDriver, String accountName,
            String pass) throws ServiceValidationException,
            LogiwebServiceException;

    /**
     * Remove driver.
     * 
     * @param driverId
     * @throws ServiceValidationException
     *             if driver is attached to truck.
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    void removeDriverAndAccount(int driverId) throws LogiwebServiceException;

    /**
     * Find drivers that are not assign to trucks. find them by city. Filter out
     * drivers who don't have enough working time to complete this order.
     * 
     * If there is not enough time in this month to finish order and if it wont
     * take more hours in next month than allowed by business rules for driver
     * then we limit hours to what is left in this month.
     * 
     * @param city
     * @param deliveryTime
     *            -- time to deliver
     * @return drivers or empty set
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    Set<Driver> findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(
            City city, float deliveryTime) throws LogiwebServiceException;

    /**
     * Calculate working hours for driver for this month.
     * 
     * Shift records that are started in previous month are trimmed. (Date of
     * start is set to first day of the this month and 00:00 hours)
     * 
     * Records that don't have ending date (meaning that driver is currently on
     * shift) are also counted. End time for them is current time.
     * 
     * @param driverId
     * @return
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    float calculateWorkingHoursForDriver(int driverId)
            throws LogiwebServiceException;

    /**
     * Assign driver to truck.
     * 
     * @param driverId
     * @param truckId
     * @throws ServiceValidationException
     *             if truck or diver not exist, or if truck already have full
     *             crew assigned
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    void assignDriverToTruck(int driverId, int truckId)
            throws LogiwebServiceException;

    /**
     * Find shift records that are started or ended in this month. Records are
     * not trimmed. (Meaning that if record is started in previous month then it
     * will be show 'as is'.
     * 
     * @param driverId
     * @return shift records or empty set
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    Set<DriverShiftJournal> findDriverJournalsForThisMonth(int driverId)
            throws LogiwebServiceException;

    void setDriverStatusToDriving(int driverEpmloyeeId)
            throws RecordNotFoundServiceException, LogiwebServiceException;

    void setDriverStatusToResting(int driverEmployeeId)
            throws RecordNotFoundServiceException, LogiwebServiceException;

    /**
     * Start new shift and change driver status to Resting en route.
     * 
     * @param driverEmloyeeId
     * @throws ServiceValidationException
     *             if unfinished shift for this driver is exist. Or if driver
     *             does not exist. Or if driver status is not FREE.
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    void startShiftForDriverAndSetRestingEnRouteStatus(int driverEmloyeeId)
            throws ServiceValidationException, LogiwebServiceException;

    /**
     * End shift and change driver status to Free.
     * 
     * @param driverEmloyeeId
     * @throws ServiceValidationException
     *             if there is no unfinished shift for this driver. Or if driver
     *             does not exist.
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    void endShiftForDriverAndSetFreeStatus(int driverEmloyeeId)
            throws ServiceValidationException, LogiwebServiceException;
}
