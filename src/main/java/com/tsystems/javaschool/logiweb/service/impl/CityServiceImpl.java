package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.CityDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

public class CityServiceImpl implements CityService {
    
    private static final Logger LOG = Logger.getLogger(CityServiceImpl.class);

    private EntityManager em;
    
    private CityDao cityDao;

    public CityServiceImpl(EntityManager em, CityDao cityDao) {
        this.cityDao = cityDao;
        this.em = em;
    }
    
    private EntityManager getEntityManager() {
        return em;
    }

    @Override
    public City findById(int id) throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            City city = cityDao.find(id);
            getEntityManager().getTransaction().commit();
            return city;
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }
    
    @Override
    public Set<City> findAllCities() throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Set<City> cities = cityDao.findAll();
            getEntityManager().getTransaction().commit();
            return cities;
        } catch (DaoException e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

}
