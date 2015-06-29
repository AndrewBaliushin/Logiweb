package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.property.Getter;

import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoExceptionCode;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.status.DriverStatus;
import com.tsystems.javaschool.logiweb.model.status.TruckStatus;
import com.tsystems.javaschool.logiweb.service.DriversService;

public class DriverServiceImpl extends GenericServiceImpl implements DriversService {
    
    private DriverDao driverDao;
    
    public DriverServiceImpl(DriverDao driverDao, EntityManager em) {
	super(em);
	this.driverDao = driverDao;
    }

    @Override
    public Set<Driver> findAllDrivers() throws DaoException {
	getEntityManager().getTransaction().begin();
	Set<Driver> drivers = driverDao.findAll();
	getEntityManager().getTransaction().commit();
	return drivers;
    }

    @Override
    public Driver findDriverById(int id) throws DaoException {
	getEntityManager().getTransaction().begin();
	Driver driver = driverDao.find(id);
	getEntityManager().getTransaction().commit();
	return driver;
    }

    @Override
    public void editDriver(Driver editedDriver) throws DaoException {
	updateOrAddDriver(editedDriver, true);
    }
    
    @Override
    public void addDriver(Driver newDriver) throws DaoException {
        updateOrAddDriver(newDriver, false);
    }

    /**
     * Create or update Driver entity.
     * 
     * @param driver
     * @param update if true -- update. if false -- create new
     * @throws DaoException if employee id is occupied or //TODO add reasons
     */
    private void updateOrAddDriver(Driver driver, boolean update) throws DaoException{
	if (isEmployeeIdAvailiable(driver)) {
	    getEntityManager().getTransaction().begin();
	    if (update) {
		driverDao.update(driver);
	    } else {
		driverDao.create(driver);
	    }
	    getEntityManager().getTransaction().commit();
	} else {
	    throw new DaoException(DaoExceptionCode.UPDATE_FAILED,
		    "Employee id " + driver.getEmployeeId()
		            + " is occupied");
	}
    }
    
    /**
     * Checks that employee id for this Driver entity is free or already belongs to this Driver.
     * 
     * @param driverToCheck
     * @return true if employee id is unoccupied or already belongs to this driver.
     * @throws DaoException if multiple result is found.
     */
    private boolean isEmployeeIdAvailiable(Driver driverToCheck) throws DaoException {
	int idInDb = driverToCheck.getId();
	int employeeId = driverToCheck.getEmployeeId();
	
	try {
	getEntityManager().getTransaction().begin();
	Driver driver = driverDao.findByEmployeeId(employeeId);
	getEntityManager().getTransaction().commit();
	
	return (driver.getId() == idInDb);
	} catch (DaoException e) {
	    if (e.getExceptionCode() == DaoExceptionCode.NO_RESULT) {
		return true;	//employeeId is free
	    }
	    else {
		throw e;
	    }
	}
    }

    @Override
    public void removeDriver(Driver driverToRemove) throws DaoException {
	if (driverToRemove.getId() == 0) { // was not assigned
	    throw new DaoException(DaoExceptionCode.REMOVE_FAILED,
		    "Can't remove driver with unassigned id.");
	}
	
	getEntityManager().getTransaction().begin();
	Driver managedDriverToRemove = driverDao.find(driverToRemove.getId());
	
	if(managedDriverToRemove == null) {
	    getEntityManager().getTransaction().rollback();
	    throw new DaoException(DaoExceptionCode.REMOVE_FAILED,
		    "Driver with id=" + driverToRemove.getId() + " not found.");
	}
	
	if (managedDriverToRemove.getCurrentTruck() != null) {
	    getEntityManager().getTransaction().rollback();
	    throw new DaoException(DaoExceptionCode.REMOVE_FAILED,
		    "Driver is assigned to Truck. Removal is forbiden.");
	}
	
	driverDao.delete(managedDriverToRemove);
	getEntityManager().getTransaction().commit();
    }

    @Override
    public Set<Driver> findAvailiableDriversForOrder(DeliveryOrder order)
	    throws DaoException {
	// TODO add method
	return null;
    }

}
