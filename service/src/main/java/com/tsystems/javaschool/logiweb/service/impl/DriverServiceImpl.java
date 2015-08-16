package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsystems.javaschool.logiweb.dao.CityDao;
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
import com.tsystems.javaschool.logiweb.model.ext.ModelToEntityConverter;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.UserService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.utils.DateUtils;

/**
 * Data manipulation and business logic related to Drivers.
 * 
 * @author Andrey Baliushina
 */
@Service
public class DriverServiceImpl implements DriverService {
    
    private static final Logger LOG = Logger.getLogger(DriverServiceImpl.class);
    
    private DriverDao driverDao;
    private TruckDao truckDao;
    private DriverShiftJournaDao driverShiftJournalDao;
    private UserService userService;
    private UserDao userDao;
    private CityDao cityDao;
      
    @Autowired
    public DriverServiceImpl(DriverDao driverDao, TruckDao truckDao,
            DriverShiftJournaDao shiftDao, UserService userService, UserDao userDao, CityDao cityDao) {
        this.driverDao = driverDao;
        this.truckDao = truckDao;
        this.driverShiftJournalDao = shiftDao;
	this.userService = userService;
	this.userDao = userDao;
	this.cityDao = cityDao;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Set<DriverModel> findAllDrivers() throws LogiwebServiceException {
        try {
            return ModelToEntityConverter.convertDriversToModels(driverDao.findAll());
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
    public DriverModel findDriverById(int id) throws LogiwebServiceException {
        try {
            Driver d = driverDao.find(id);
            if (d != null) {
                return ModelToEntityConverter.convertToModel(d);
            } else {
                return null;
            }
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
            
            Driver driverEntityToEdit = driverDao.find(editedDriver.getId());
            if(driverEntityToEdit == null) {
                throw new ServiceValidationException("Driver record not found.");
            }
            
            populateAllowedDriverFieldsFromModel(driverEntityToEdit,
                    editedDriver);
            
            driverDao.update(driverEntityToEdit);
            LogiwebUser account = driverEntityToEdit.getLogiwebAccount();
            account.setMail(newAccountName);

            LOG.info("Driver edited. " + driverEntityToEdit.getName() + " "
                    + driverEntityToEdit.getSurname() + " ID#" + driverEntityToEdit.getId());
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
     * Add Driver and link new driver to provided driver account.
     * 
     * @param new Driver as model
     * @param existing account
     * @return id in db of created driver
     * @throws ServiceValidationException if driver don't have all required fields or have not unique employee ID
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    private int addDriverAndLinkHimToAccount(DriverModel newDriverAsModel, LogiwebUser accountForDriver) throws ServiceValidationException,
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
        return addDriverAndLinkHimToAccount(newDriver, accountForDriver); //all necessary validations are inside this method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeDriverAndAccount(int driverId) throws ServiceValidationException, LogiwebServiceException {
        try {
            Driver driverToRemove = driverDao.find(driverId);

            if (driverToRemove == null) {
                throw new ServiceValidationException("Driver with id="
                        + driverId + " not found.");
            }

            if (driverToRemove.getCurrentTruck() != null) {
                throw new ServiceValidationException(
                        "Driver is assigned to Truck. Removal is forbiden.");
            }

            LogiwebUser account = driverToRemove.getLogiwebAccount();
            driverDao.delete(driverToRemove);
            userDao.delete(account);
            
            LOG.info("Driver removed with account. Employee ID#" + driverToRemove.getEmployeeId()
                    + " " + driverToRemove.getName() + " " + driverToRemove.getSurname());
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
    public Set<DriverModel> findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(
            int cityId, float workingHoursMaxLimit) throws LogiwebServiceException {
        try {
            City city = cityDao.find(cityId);
            if (city == null) {
                throw new RecordNotFoundServiceException();
            }
            
            Set<Driver> freeDriversInCity = driverDao
                    .findByCityWhereNotAssignedToTruck(city);
            Set<DriverShiftJournal> journals = driverShiftJournalDao
                    .findThisMonthJournalsForDrivers(freeDriversInCity);
            
            Map<Driver, Float> workingHoursData = sumWorkingHoursForThisMonth(journals);
            for (Driver driver : freeDriversInCity) {   //add drivers that don't yet have journals
                if(workingHoursData.get(driver) == null) {
                    workingHoursData.put(driver, 0f);
                }
            }
            
            filterDriversByMaxWorkingHours(workingHoursData, workingHoursMaxLimit);
            
            return ModelToEntityConverter.convertDriversToModels(workingHoursData.keySet());
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
    public Set<DriverShiftJournal> findDriverJournalsForThisMonth(int driverId)
            throws LogiwebServiceException {
        try {
            Driver driver = driverDao.find(driverId);
            if (driver == null) {
                return new HashSet<DriverShiftJournal>();
            } else {
                return driverShiftJournalDao
                        .findThisMonthJournalsForDrivers(driver);
            }
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
    public float calculateWorkingHoursForDriver(int driverId) throws LogiwebServiceException {
        try {
            Driver driver = driverDao.find(driverId); //get managed entity
            
            Set<DriverShiftJournal> journals = driverShiftJournalDao
                    .findThisMonthJournalsForDrivers(driver);
            Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(journals);
            
            //if driver don't have any records yet
            if (workingHours.get(driver) == null) {
                workingHours.put(driver, 0f);
            }
            
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
    
        Map<Driver, Float> workingHoursForDrivers = new HashMap<Driver, Float>();
        
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
        Iterator<Entry<Driver, Float>> it = workingHoursToFilter.entrySet()
                .iterator();
        
        while (it.hasNext()) {
            Entry<Driver, Float> e = it.next();
            if (e.getValue() > maxWorkingHours) {
                it.remove();
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
                truck.setDrivers(truckCrew);
            }
        
            if(truckCrew.size() < truck.getCrewSize()) {
                truckCrew.add(driver);
                driver.setCurrentTruck(truck);
            } else {
                throw new ServiceValidationException("All crew positions are occupied. Can't add Driver to crew.");
            }
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
    public void startShiftForDriverAndSetRestingEnRouteStatus(int driverEmloyeeId)
            throws ServiceValidationException, LogiwebServiceException {
        try {
            Driver driver = driverDao.findByEmployeeId(driverEmloyeeId);
            if (driver == null) {
                throw new ServiceValidationException(
                        "Provide valid driver employee id.");
            }
            if (driver.getStatus() != DriverStatus.FREE) {
                throw new ServiceValidationException("Driver must be free to start new shift.");
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
            
            driver.setStatus(DriverStatus.RESTING_EN_ROUT);
            driverDao.update(driver);
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } 
    }

    @Override
    @Transactional
    public void endShiftForDriverAndSetFreeStatus(int driverEmloyeeId)
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
            
            driver.setStatus(DriverStatus.FREE);
            driverDao.update(driver);
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        }
    }
}
