package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.DriverStatus;

public interface DriverDao extends GenericDao<Driver>{
    
    Set<Driver> getByWokingHoursLimit(Float hoursInThisMonthLimit);
    
    Set<Driver> getByStatus(DriverStatus status);
    
    Set<Driver> getByCurrentCityLocation(City currentCityLoation);
}
