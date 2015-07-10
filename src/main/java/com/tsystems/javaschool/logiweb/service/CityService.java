package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

/**
 * Data manipulation and business logic related to Cities.
 * 
 * @author Andrey Baliushin
 */
public interface CityService {

    /**
     * Find city by its id.
     * 
     * @param id
     * @return null if city not found
     * @throws LogiwebServiceException
     *             if something unexpected happened on lower level
     */
    City findById(int id) throws LogiwebServiceException;
    
    /**
     * Find all cities.
     * 
     * @return empty set if none found
     * @throws LogiwebServiceException
     *             if something unexpected happened on lower level
     */
    Set<City> findAllCities() throws LogiwebServiceException;
    
}
