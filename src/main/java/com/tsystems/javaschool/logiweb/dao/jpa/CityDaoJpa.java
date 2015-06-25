package com.tsystems.javaschool.logiweb.dao.jpa;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.CityDao;
import com.tsystems.javaschool.logiweb.model.City;

public class CityDaoJpa extends GenericDaoJpa<City> implements CityDao {

    public CityDaoJpa(Class<City> entityClass, EntityManager entityManager) {
	super(entityClass, entityManager);
    }

}
