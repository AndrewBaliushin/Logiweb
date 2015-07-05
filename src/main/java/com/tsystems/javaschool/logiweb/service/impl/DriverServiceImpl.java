package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.DaoFactory;
import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.DriverShiftJournaDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoExceptionCode;
import com.tsystems.javaschool.logiweb.dao.jpa.GenericDaoJpa;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.DriverShiftJournal;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.exceptions.DriverDeletionException;
import com.tsystems.javaschool.logiweb.service.exceptions.DriverEmployeeIdOccupiedException;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
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
    private DriverShiftJournaDao driverShiftJournalDao;
      
    public DriverServiceImpl(DriverDao driverDao, DriverShiftJournaDao shiftDao, EntityManager em) {
	this.em = em;
	this.driverDao = driverDao;
	this.driverShiftJournalDao = shiftDao;
    }

    private EntityManager getEntityManager() {
        return em;
    }
    
    @Override
    public Set<Driver> findAllDrivers() throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Set<Driver> drivers = driverDao.findAll();
            getEntityManager().getTransaction().commit();
            return drivers;
        } catch (DaoException e) {         //TODO figure out right way
            LOG.warn("Something wrong...");
            throw new LogiwebServiceException(); //TODO change to specific
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

    @Override
    public Driver findDriverById(int id) throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Driver driver = driverDao.find(id);
            getEntityManager().getTransaction().commit();
            return driver;
        } catch (DaoException e) { // TODO figure out right way
            LOG.warn("Something wrong...");
            throw new LogiwebServiceException(); // TODO change to specific
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

    @Override
    public void editDriver(Driver editedDriver) throws LogiwebServiceException {
	updateOrAddDriver(editedDriver, true);
    }
    
    @Override
    public void addDriver(Driver newDriver) throws LogiwebServiceException {
        updateOrAddDriver(newDriver, false);
    }

    /**
     * Create or update Driver entity.
     * 
     * @param driver
     * @param update if true -- update. if false -- create new
     * @throws DaoException if employee id is occupied or //TODO add reasons
     */
    private void updateOrAddDriver(Driver driver, boolean update)
            throws LogiwebServiceException {
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
                throw new DriverEmployeeIdOccupiedException();
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

    @Override
    public void removeDriver(Driver driverToRemove) throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Driver managedDriverToRemove = driverDao.find(driverToRemove
                    .getId());

            if (managedDriverToRemove == null) {
                getEntityManager().getTransaction().rollback();
                throw new DriverDeletionException("Driver with id="
                        + driverToRemove.getId() + " not found.");
            }

            if (managedDriverToRemove.getCurrentTruck() != null) {
                getEntityManager().getTransaction().rollback();
                throw new DriverDeletionException(
                        "Driver is assigned to Truck. Removal is forbiden.");
            }

            driverDao.delete(managedDriverToRemove);
            getEntityManager().getTransaction().commit();
        } catch (DaoException e) {
            LOG.warn("Something wrong..."); // TODO ????????
            throw new LogiwebServiceException();
        }
    }

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

    @Override
    public float calculateWorkingHoursForDriver(Driver driver) {
        Set<DriverShiftJournal> journals = driverShiftJournalDao
                .findThisMonthJournalsForDrivers(driver);
        Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(journals);
        
        //if driver don't have any records yet
        if(workingHours.get(driver) == null) workingHours.put(driver, 0f);
        
        return workingHours.get(driver);
    }
    
    /**
     * Calculate total working hours for Drivers that are listed in Shift journal.
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
    
}
