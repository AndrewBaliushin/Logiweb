package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

public interface DriversService {
    
    Set<Driver> findAllDrivers() throws LogiwebServiceException;

    Driver findDriverById(int id) throws LogiwebServiceException;

    void editDriver(Driver editedDriver) throws LogiwebServiceException;

    void addDriver(Driver newDriver) throws LogiwebServiceException;
    
    void removeDriver(Driver driverToRemove) throws LogiwebServiceException;
    
}
