package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.Set;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.DriverStatus;

public class DriverDaoJpa extends GenericDaoJpa<Driver> implements DriverDao {

    public DriverDaoJpa(Class<Driver> entityClass, EntityManager entityManager) {
	super(entityClass, entityManager);
    }

    @Override
    public Set<Driver> getAvailiable(float maxWorkingHours, City inCity,
	    DriverStatus status) {
	// TODO Auto-generated method stub
	return null;
    }

}
