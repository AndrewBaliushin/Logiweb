package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.DriverShiftJournaDao;
import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.UserDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.DriverShiftJournal;
import com.tsystems.javaschool.logiweb.entities.LogiwebUser;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.DriverStatus;
import com.tsystems.javaschool.logiweb.entities.status.UserRole;
import com.tsystems.javaschool.logiweb.model.DriverModel;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.UserService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.utils.DateUtils;

/**
 * Data manipulation and business logic related to Drivers.
 * 
 * @author Andrey Baliushin
 */
@Service
public class DriverServiceImpl implements DriverService {
    
    private static final Logger LOG = Logger.getLogger(DriverServiceImpl.class);
    
    private DriverDao driverDao;
    private TruckDao truckDao;
    private DriverShiftJournaDao driverShiftJournalDao;
    private UserService userService;
    private UserDao userDao;
      
    @Autowired
    public DriverServiceImpl(DriverDao driverDao, TruckDao truckDao,
            DriverShiftJournaDao shiftDao, UserService userService, UserDao userDao) {
        this.driverDao = driverDao;
        this.truckDao = truckDao;
        this.driverShiftJournalDao = shiftDao;
	this.userService = userService;
	this.userDao = userDao;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Set<Driver> findAllDrivers() throws LogiwebServiceException {
        try {
            return driverDao.findAll();
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        } 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Driver findDriverById(int id) throws LogiwebServiceException {
        try {
            return driverDao.find(id);
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Driver findDriverByEmployeeId(int employeeId) throws LogiwebServiceException {
        try {
            return driverDao.findByEmployeeId(employeeId);
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     * @throws DaoException 
     */
    @Override
    @Transactional
    public void editDriverAndAccountName(DriverModel editedDriver, String newAccountName) throws ServiceValidationException, LogiwebServiceException {
        if(editedDriver.getId() == null || editedDriver.getId() <= 0) {
            throw new ServiceValidationException("Driver id isn't provided.");
        }
        
        try {
            Driver driverWithSameEmployeeId = driverDao
                    .findByEmployeeId(editedDriver.getEmployeeId());
            
            if (driverWithSameEmployeeId != null && driverWithSameEmployeeId.getId() != editedDriver.getId()) {
                throw new ServiceValidationException("Employee ID  #"
                        + editedDriver.getEmployeeId() + " is already in use.");
            }
            
            Driver driverEnitytyToEdit = driverDao.find(editedDriver.getId());
            if(driverEnitytyToEdit == null) {
                throw new ServiceValidationException("Driver record not found.");
            }
            
            populateAllowedDriverFieldsFromModel(driverEnitytyToEdit,
                    editedDriver);
            
            driverDao.update(driverEnitytyToEdit);
            LogiwebUser account = driverEnitytyToEdit.getLogiwebAccount();
            account.setMail(newAccountName);

            LOG.info("Driver edited. " + driverEnitytyToEdit.getName() + " "
                    + driverEnitytyToEdit.getSurname() + " ID#" + driverEnitytyToEdit.getId());
        } catch (DaoException e) {
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }
    
    /**
     * Populate fields that are used to edit or create new driver.
     * (city, name, surname, employee id, status)
     * @param driverToPopulate
     * @param source DriverModel
     * @return
     */
    private Driver populateAllowedDriverFieldsFromModel(Driver driverToPopulate, DriverModel source) {
        City city = new City();
        city.setId(source.getCurrentCityId());
        
        driverToPopulate.setCurrentCity(city);
        driverToPopulate.setEmployeeId(source.getEmployeeId());
        driverToPopulate.setSurname(source.getSurname());
        driverToPopulate.setName(source.getName());
        driverToPopulate.setStatus(source.getStatus());
        
        return driverToPopulate;
    }
    
    /**
     * Add Driver.
     * 
     * @param new Driver as model
     * @return id in db of created driver
     * @throws ServiceValidationException if driver don't have all required fields or have not unique employee ID
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    @Transactional
    private int addDriver(DriverModel newDriverAsModel, LogiwebUser accountForDriver) throws ServiceValidationException,
            LogiwebServiceException {
        newDriverAsModel.setStatus(DriverStatus.FREE); //default status
        
        try {
            Driver driverWithSameEmployeeId = driverDao
                    .findByEmployeeId(newDriverAsModel.getEmployeeId());

            if (driverWithSameEmployeeId != null) {
                throw new ServiceValidationException("Employee ID  #"
                        + newDriverAsModel.getEmployeeId() + " is already in use.");
            }
            
            Driver newDriverEnity = new Driver();
            
            populateAllowedDriverFieldsFromModel(newDriverEnity, newDriverAsModel);
            newDriverEnity.setLogiwebAccount(accountForDriver);
            
            driverDao.create(newDriverEnity);

            LOG.info("Driver created. " + newDriverEnity.getName() + " "
                    + newDriverEnity.getSurname()
                    + " ID#" + newDriverEnity.getId());
            
            return newDriverEnity.getId();
        } catch (DaoException e) {         
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int addDriverWithAccount(DriverModel newDriver, String accountName, String pass)
            throws ServiceValidationException, LogiwebServiceException {
        int newUserId = userService.createNewUser(accountName, pass, UserRole.ROLE_DRIVER);
        LogiwebUser accountForDriver = userService.findUserById(newUserId); 
        int driverId = addDriver(newDriver, accountForDriver); //all necessary validations are inside this method
        
        return driverId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeDriverAndAccount(Driver driverToRemove) throws ServiceValidationException, LogiwebServiceException {
        try {
            Driver managedDriverToRemove = driverDao.find(driverToRemove
                    .getId());

            if (managedDriverToRemove == null) {
                throw new ServiceValidationException("Driver with id="
                        + driverToRemove.getId() + " not found.");
            }

            if (managedDriverToRemove.getCurrentTruck() != null) {
                throw new ServiceValidationException(
                        "Driver is assigned to Truck. Removal is forbiden.");
            }
            

            LogiwebUser account = managedDriverToRemove.getLogiwebAccount();
            driverDao.delete(managedDriverToRemove);
            userDao.delete(account);
            
            LOG.info("Driver removed with account. Employee ID#" + managedDriverToRemove.getEmployeeId()
                    + " " + managedDriverToRemove.getName() + " " + managedDriverToRemove.getSurname());
        } catch (DaoException e) {         
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Set<Driver> findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(
            City city, float maxWorkingHours) throws LogiwebServiceException {
        try {
            Set<Driver> freeDriversInCity = driverDao
                    .findByCityWhereNotAssignedToTruck(city);
            Set<DriverShiftJournal> journals = driverShiftJournalDao
                    .findThisMonthJournalsForDrivers(freeDriversInCity);
            
            Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(journals);
            for (Driver driver : freeDriversInCity) {   //add drivers that don't yet have journals
                if(workingHours.get(driver) == null) workingHours.put(driver, 0f);
            }
            
            filterDriversByMaxWorkingHours(workingHours, maxWorkingHours);
            
            return workingHours.keySet();
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } 
    }
    
    /**
     * {@inheritDoc}
     * @throws LogiwebServiceException 
     */
    @Override
    @Transactional
    public Set<DriverShiftJournal> findDriverJournalsForThisMonth(Driver driver)
            throws LogiwebServiceException {
        try {
            return driverShiftJournalDao
                    .findThisMonthJournalsForDrivers(driver);
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } 
    }
    
    /**
     * {@inheritDoc}
     * @throws LogiwebServiceException 
     */
    @Override
    @Transactional
    public float calculateWorkingHoursForDriver(Driver driver) throws LogiwebServiceException {
        try {
            driver = driverDao.find(driver.getId()); //get managed entity
            
            Set<DriverShiftJournal> journals = driverShiftJournalDao
                    .findThisMonthJournalsForDrivers(driver);
            Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(journals);
            
            //if driver don't have any records yet
            if (workingHours.get(driver) == null) workingHours.put(driver, 0f);
            
            return workingHours.get(driver);
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        }
    }
    
    /**
     * Calculate total working hours for Drivers that are listed in Shift journals.
     * 
     * @param journal
     * @return Map with Driver as keys and working hours as values.
     */
    private Map<Driver, Float> sumWorkingHoursForThisMonth(
            Collection<DriverShiftJournal> journal) {
    
        Map<Driver, Float> workingHoursForDrivers = new ConcurrentHashMap<Driver, Float>();
        
        Date firstDayOfCurrentMonth = DateUtils.getFirstDateOfCurrentMonth();
        Date firstDayOfNextMonth = DateUtils.getFirstDayOfNextMonth();
        
        for (DriverShiftJournal j : journal) {
            Driver driver = j.getDriverForThisRecord();
            Date shiftBeggined = j.getShiftBeggined();
            Date shiftEnded = j.getShiftEnded();
            
            /*
             * If shift ended after or before current month
             * we trim it by limiting shift end to first day of next
             * month (00:00:00) and  shift start to first day of month (00:00:00).
             * if shift is not yet ended -- set Now as upper limit.            * 
             */
            if (shiftEnded == null) {
                shiftEnded = new Date();
            }
            if (shiftBeggined.getTime() < firstDayOfCurrentMonth.getTime()) {
                shiftBeggined = firstDayOfCurrentMonth;
            }
            if (shiftEnded.getTime() > firstDayOfNextMonth.getTime()) {
                shiftEnded = firstDayOfNextMonth;
            }
            
            float shiftDuration = DateUtils.diffInHours(shiftBeggined, shiftEnded);
            Float totalHoursForDriver = workingHoursForDrivers.get(driver);
            
            if(totalHoursForDriver == null) {
                workingHoursForDrivers.put(driver, shiftDuration);
            } else {
                workingHoursForDrivers.put(driver, totalHoursForDriver + shiftDuration);
            }
        }
        
        return workingHoursForDrivers;        
    }
    
    /**
     * Filter Map of working hours records.
     * Delete entry if limit of hours is exceeded.
     * 
     * @param workingHoursToFilter
     * @param maxWorkingHours
     */
    private void filterDriversByMaxWorkingHours(
            Map<Driver, Float> workingHoursToFilter, float maxWorkingHours) {
        for (Entry<Driver, Float> e : workingHoursToFilter.entrySet()) {
            if (e.getValue() > maxWorkingHours) {
                workingHoursToFilter.remove(e.getKey());
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void assignDriverToTruck(int driverId, int truckId) throws ServiceValidationException, LogiwebServiceException {
        try {
            Driver driver = driverDao.find(driverId);
            Truck truck = truckDao.find(truckId);
            
            if(driver == null || truck == null) {
                throw new ServiceValidationException("Driver and truck must exist.");
            }
            
            Set<Driver> truckCrew = truck.getDrivers();
            if(truckCrew == null) {
                truckCrew = new HashSet<Driver>();
            }
        
            if(truckCrew.size() < truck.getCrewSize()) {
                truckCrew.add(driver);
                driver.setCurrentTruck(truck);
            } else {
                throw new ServiceValidationException("All crew positions are occupied. Can't add Driver to crew.");
            }
        } catch (ServiceValidationException e) {
            throw e;
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public void setDriverStatusToDriving(int driverEmployeeId)
            throws com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException,
            LogiwebServiceException {
        changeDriverStatus(driverEmployeeId, DriverStatus.DRIVING);
        
    }

    @Override
    @Transactional
    public void setDriverStatusToResting(int driverEmployeeId)
            throws RecordNotFoundServiceException,
            LogiwebServiceException {
        changeDriverStatus(driverEmployeeId, DriverStatus.RESTING_EN_ROUT);
    }
    
    private void changeDriverStatus(int driverEmployeeId, DriverStatus newStatus) throws RecordNotFoundServiceException, LogiwebServiceException {
        try {
            Driver driver = driverDao.findByEmployeeId(driverEmployeeId);
            if (driver == null) {
                throw new RecordNotFoundServiceException();
            }
            driver.setStatus(newStatus);
            driverDao.update(driver);            
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public void startShiftForDriver(int driverEmloyeeId)
            throws ServiceValidationException, LogiwebServiceException {
        try {
            Driver driver = driverDao.findByEmployeeId(driverEmloyeeId);
            if (driver == null) {
                throw new ServiceValidationException(
                        "Provide valid driver employee id.");
            }
            DriverShiftJournal unfinishedShift = driverShiftJournalDao
                    .findUnfinishedShiftForDriver(driver);

            if (unfinishedShift != null) {
                throw new ServiceValidationException(
                        "Finish existing shift before creating new one.");
            }

            DriverShiftJournal newShift = new DriverShiftJournal();
            newShift.setDriverForThisRecord(driver);
            newShift.setShiftBeggined(new Date()); // now

            driverShiftJournalDao.create(newShift);
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } 
    }

    @Override
    @Transactional
    public void endShiftForDriver(int driverEmloyeeId)
            throws ServiceValidationException, LogiwebServiceException {
        try {
            Driver driver = driverDao.findByEmployeeId(driverEmloyeeId);
            if (driver == null) {
                throw new ServiceValidationException(
                        "Provide valid driver employee id.");
            }
            DriverShiftJournal unfinishedShift = driverShiftJournalDao
                    .findUnfinishedShiftForDriver(driver);

            if (unfinishedShift == null) {
                throw new ServiceValidationException(
                        "There is no active shift for this driver.");
            }
            
            unfinishedShift.setShiftEnded(new Date());
            driverShiftJournalDao.update(unfinishedShift);   
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        }
    }
}
