package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.DriverShiftJournaDao;
import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.DriverShiftJournal;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.model.status.DriverStatus;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.utils.DateUtils;

/**
 * Data manipulation and business logic related to Drivers.
 * 
 * @author Andrey Baliushin
 */
public class DriverServiceImpl implements DriverService {
    
    private static final Logger LOG = Logger.getLogger(DriverServiceImpl.class);
    
    private EntityManager em;
    
    private DriverDao driverDao;
    private TruckDao truckDao;
    private DriverShiftJournaDao driverShiftJournalDao;
      
    public DriverServiceImpl(DriverDao driverDao, TruckDao truckDao,DriverShiftJournaDao shiftDao, EntityManager em) {
	this.em = em;
	this.driverDao = driverDao;
	this.truckDao = truckDao;
	this.driverShiftJournalDao = shiftDao;
    }

    private EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Driver> findAllDrivers() throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Set<Driver> drivers = driverDao.findAll();
            getEntityManager().getTransaction().commit();
            return drivers;
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Driver findDriverById(int id) throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Driver driver = driverDao.find(id);
            getEntityManager().getTransaction().commit();
            return driver;
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editDriver(Driver editedDriver) throws LogiwebServiceException {
	updateOrAddDriver(editedDriver, true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Driver addDriver(Driver newDriver) throws ServiceValidationException,
            LogiwebServiceException {
        newDriver.setStatus(DriverStatus.FREE);
        
        try {
            validateForEmptyFields(newDriver); 
        } catch (ServiceValidationException e) {
            throw e;
        }
        
        try {
            getEntityManager().getTransaction().begin();
            Driver driverWithSameEmployeeId = driverDao
                    .findByEmployeeId(newDriver.getEmployeeId());

            if (driverWithSameEmployeeId != null) {
                throw new ServiceValidationException("Employee ID  #"
                        + newDriver.getEmployeeId() + " is already in use.");
            }
            
            driverDao.create(newDriver);
            getEntityManager().refresh(newDriver);
            getEntityManager().getTransaction().commit();

            LOG.info("Driver created. " + newDriver.getName() + " "
                    + newDriver.getSurname()
                    + " ID#" + newDriver.getId());

            return newDriver;
        } catch (ServiceValidationException e) {
            throw e;        
        } catch (DaoException e) {         
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }

    }
    
    /**
     * Check if driver has empty fields that should not be empty.
     * @param d Driver
     * @return Doesn't return anything -- throws exception if failed.
     * @throws ServiceValidationException with message that describes why validation failed.
     */
    private void validateForEmptyFields(Driver d) throws ServiceValidationException {
        if(d.getEmployeeId() <= 0) {
            throw new ServiceValidationException("Employee ID can't be 0 or negative.");
        } else if (StringUtils.isBlank(d.getName())) {
            throw new ServiceValidationException("Drivers name can't be empty.");
        } else if (StringUtils.isBlank(d.getSurname())) {
            throw new ServiceValidationException("Drivers surname can't be empty.");
        } else if (d.getCurrentCity() == null || d.getCurrentCity().getId() == 0) {
            throw new ServiceValidationException("City is not set.");
        } 
    }

    /**
     * Create or update Driver entity.
     * 
     * @param driver
     * @param update if true -- update. if false -- create new
     * @throws DaoException if employee id is occupied or //TODO add reasons
     */
    private void updateOrAddDriver(Driver driver, boolean update)
            throws ServiceValidationException, LogiwebServiceException {
        try {
            if (isEmployeeIdAvailiable(driver)) {
                getEntityManager().getTransaction().begin();
                if (update) {
                    driverDao.update(driver);
                } else {
                    driverDao.create(driver);
                }
                getEntityManager().getTransaction().commit();
            } else {
                throw new ServiceValidationException("Employee id "
                        + " occupied");
            }
        } catch (DaoException e) { 
            LOG.warn("Something wrong..."); //TODO ????????
            throw new LogiwebServiceException();
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }
    
    /**
     * Checks that employee id for this Driver entity is free or already belongs to this Driver.
     * 
     * @param driverToCheck
     * @return true if employee id is unoccupied or already belongs to this driver.
     * @throws DaoException if multiple result is found.
     */
    private boolean isEmployeeIdAvailiable(Driver driverToCheck) throws DaoException{
        int employeeId = driverToCheck.getEmployeeId();

        getEntityManager().getTransaction().begin();
        Driver driver = driverDao.findByEmployeeId(employeeId);
        getEntityManager().getTransaction().commit();

        return driver == null || driver.getId() == driverToCheck.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeDriver(Driver driverToRemove) throws ServiceValidationException, LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
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

            driverDao.delete(managedDriverToRemove);
            getEntityManager().getTransaction().commit();
            LOG.info("Driver removed. Employee ID#" + managedDriverToRemove.getEmployeeId()
                    + " " + managedDriverToRemove.getName() + " " + managedDriverToRemove.getSurname());
        } catch (ServiceValidationException e) {
            throw e;        
        } catch (DaoException e) {         
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Driver> findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(
            City city, float maxWorkingHours) throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Set<Driver> freeDriversInCity = driverDao
                    .findByCityWhereNotAssignedToTruck(city);
            Set<DriverShiftJournal> journals = driverShiftJournalDao
                    .findThisMonthJournalsForDrivers(freeDriversInCity);
            getEntityManager().getTransaction().commit();
            
            Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(journals);
            for (Driver driver : freeDriversInCity) {   //add drivers that don't yet have journals
                if(workingHours.get(driver) == null) workingHours.put(driver, 0f);
            }
            
            filterDriversByMaxWorkingHours(workingHours, maxWorkingHours);
            
            return workingHours.keySet();
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } catch (Exception e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }
    
    /**
     * {@inheritDoc}
     * @throws LogiwebServiceException 
     */
    @Override
    public Set<DriverShiftJournal> findDriverJournalsForThisMonth(Driver driver)
            throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Set<DriverShiftJournal> journals = driverShiftJournalDao
                    .findThisMonthJournalsForDrivers(driver);
            getEntityManager().getTransaction().commit();

            return journals;
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } catch (Exception e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
        
        
    }
    
    /**
     * {@inheritDoc}
     * @throws LogiwebServiceException 
     */
    @Override
    public float calculateWorkingHoursForDriver(Driver driver) throws LogiwebServiceException {
        try {
            Set<DriverShiftJournal> journals = driverShiftJournalDao
                    .findThisMonthJournalsForDrivers(driver);
            Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(journals);
            
            //if driver don't have any records yet
            if (workingHours.get(driver) == null) workingHours.put(driver, 0f);
            
            return workingHours.get(driver);
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } catch (Exception e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
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
            if (e.getValue() >= maxWorkingHours) {
                workingHoursToFilter.remove(e.getKey());
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assignDriverToTruck(int driverId, int truckId) throws ServiceValidationException, LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
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
            
            getEntityManager().getTransaction().commit();
        } catch (ServiceValidationException e) {
            throw e;
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } catch (Exception e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }
    
}
