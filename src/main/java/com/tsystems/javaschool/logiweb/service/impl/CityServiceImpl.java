package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsystems.javaschool.logiweb.dao.CityDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

/**
 * Data manipulation and business logic related to Cities.
 * 
 * @author Andrey Baliushin
 */
@Service
public class CityServiceImpl implements CityService {
    
    private static final Logger LOG = Logger.getLogger(CityServiceImpl.class);
    
    private CityDao cityDao;

    @Autowired
    public CityServiceImpl(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    @Transactional
    public City findById(int id) throws LogiwebServiceException {
        try {
            return cityDao.find(id);
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }
    
    @Override
    public Set<City> findAllCities() throws LogiwebServiceException {
        try {
            return cityDao.findAll();
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }

}
