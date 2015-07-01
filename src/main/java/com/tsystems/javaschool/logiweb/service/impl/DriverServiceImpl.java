package com.tsystems.javaschool.logiweb.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.DaoFactory;
import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoExceptionCode;
import com.tsystems.javaschool.logiweb.dao.jpa.GenericDaoJpa;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.service.DriversService;
import com.tsystems.javaschool.logiweb.service.exceptions.DriverDeletionException;
import com.tsystems.javaschool.logiweb.service.exceptions.DriverEmployeeIdOccupiedException;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

public class DriverServiceImpl implements DriversService {
    
    private static final Logger LOG = Logger.getLogger(GenericDaoJpa.class);
    
    private EntityManager em;
    
    private DriverDao driverDao;
    private DeliveryOrderDao deliveryOrderDao;
      
    public DriverServiceImpl(DaoFactory daoFactory, EntityManager em) {
	this.em = em;
	this.driverDao = daoFactory.getDriverDao();
	this.deliveryOrderDao = daoFactory.getDeliveryOrderDao();
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
    
}
