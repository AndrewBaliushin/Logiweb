package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Set;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Driver;
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
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void editDriver(Driver editedDriver) throws DaoException {
	// TODO Auto-generated method stub

    }

    @Override
    public void addDriver(Driver newDriver) throws DaoException {
	// TODO Auto-generated method stub

    }

    @Override
    public void removeDriver(Driver driverToRemove) throws DaoException {
	// TODO Auto-generated method stub

    }

    @Override
    public Set<Driver> findAvailiableDriversForOrder(DeliveryOrder order)
	    throws DaoException {
	// TODO Auto-generated method stub
	return null;
    }

}
