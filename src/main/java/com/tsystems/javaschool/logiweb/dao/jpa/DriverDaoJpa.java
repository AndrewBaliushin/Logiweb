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
    public Set<Driver> getByWokingHoursLimit(Float hoursInThisMonthLimit) {
	
	return null;
    }

    @Override
    public Set<Driver> getByStatus(DriverStatus status) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Set<Driver> getByCurrentCityLocation(City currentCityLoation) {
	// TODO Auto-generated method stub
	return null;
    }

}
