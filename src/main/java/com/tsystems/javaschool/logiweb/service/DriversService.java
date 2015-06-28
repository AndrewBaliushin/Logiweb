package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Driver;

public interface DriversService {
    
    Set<Driver> findAllDrivers() throws DaoException;

    Driver findDriverById(int id) throws DaoException;

    void editDriver(Driver editedDriver) throws DaoException;

    void addDriver(Driver newDriver) throws DaoException;
    
    void removeDriver(Driver driverToRemove) throws DaoException;
    
    Set<Driver> findAvailiableDriversForOrder(DeliveryOrder order) throws DaoException;
}
