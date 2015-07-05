package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

public interface CityService {

    City findById(int id) throws LogiwebServiceException;
    
    Set<City> findAllCities() throws LogiwebServiceException;
    
}
